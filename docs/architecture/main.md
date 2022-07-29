## HDFS 写数据流程
1. HDFS 客户端创建 FileSystem 对象实例 DistributedFileSystem，FileSystem 封装了与文件系统操作相关的方法。
2. 调用 DistributedFileSystem 的 create() 方法，通过 RPC 请求 NameNode 创建文件。
NameNode 执行各种检查判断：是否有权限，目标文件是否已经存在等。检查通过后 NameNode 就会为本次请求记下一条记录，返回 DFSDataOutputStream 
输出流对象给客户端用于写数据。
3. 客户端通过 FSDataOutputStream 开始写入数据
4. 客户端写入数据时，FSDataOutputStream 会将数据分成一个一个数据包（packet 默认 64k），并写入一个内部数据队列（data queue）。
DFSDataOutputStream 有一个内部类 DataStreamer，用于请求 NameNode 挑选出适合存储数据副本的一组 DataNode，默认是 3 副本存储。DataStreamer
将数据包流式传输到 Pipeline 的第一个 DataNode，该 DataNode 接收到数据之后再把数据发送到下一个 DataNode 中，直到 3 副本都接收到数据为止。
5. DFSDataOutputStream 也维护者一个内部数据包队列来等待 DataNode 的收到确认回执，称之为确认队列。收到 Pipeline 中所有 DataNode 确认信息后，
该数据包才会从确认队列删除。
6. 客户端完成数据写入后，在 DFSDataOutputStream 输出流上调用 close() 方法来关闭输出流。
7. DistributedFileSystem 联系 NameNode 告知其文件写入完成，等待 NameNode 确认。
因为 NameNode 已经知道文件由哪些块组成，因此仅需要等待最小复制块即可成功返回。最小复制数是由参数 dfs.namenode.replication.min 指定，默认为 1。
表示只要成功写入一个副本，NameNode 就会给客户端返回成功响应。

## NameNode 元数据管理
#### 元数据管理概述
在 HDFS 中，文件相关元数据有两种类型：
* 文件自身属性信息：文件名称，权限，修改时间，文件大小，复制因子，数据块大小。
* 文件块位置映射信息：记录文件块存储在哪个 DataNode 上。

按照存储形式区分，元数据分为存储在内存中的内存元数据和存储在磁盘上的元数据文件。其中元数据文件有两种：fsimage 内存镜像文件，Edits log 编辑日志。
* fsimage 内存镜像文件
fsimage 是内存元数据的一个持久化的检查点，相当于内存中文件自身属性元数据某一时刻的快照。 是由 DataNode 启动加入集群的时候，向 NameNode 
进行数据汇报的时候得到的，并且后续 DataNode 会定时进行汇报。

* Edits log 编辑日志
NameNode 会定期将内存中的文件自身属性元数据进行快照，持久化到磁盘的 fsimage 内存镜像文件中。当 NameNode 节点重启，那么内存中存储的文件自身属性信息元数据
就会部分丢失（仅能从磁盘的 fsimage 恢复部分数据），因此需要 Edits log 编辑日志。Edits log 编辑日志会记录所有的事务性操作（修改操作）。从 fsimage 中
恢复不了的数据，可以通过 Edits log 进行恢复。

![图片alt](../images/metadata.jpg)

#### HDFS format 操作
首次启动 HDFS 时需要进行 format 操作，这里的 format 并不是格式化的意思而是对 HDFS 进行初始化。包括：一些清理准备工作，创建元数据本地存储目录和
初始化元数据相关文件。

#### 元数据相关文件-VERSION
什么是 blockpool？  
blockpool 指的是属于某个 NameSpace 的一组 block。每一个 DataNode 中都含有所有的 blockpool。不同的 blockpool 中有属于各自 NameSpace 的
block。

* NameSpaceID：联邦集群下，不同的 NameNode 有它自己的 id
* clusterID：集群 id，一个集群下的所有 NameNode，DataNode 它们的集群 id 都是相同的
* blockpoolID：同一个 NameSpace 下的 block 他们的 blockpoolID 是相同的
* storageType：该节点类型
* cTime：NameNode 存储系统创建时间，首次格式化文件系统时这个属性值为 0，当文件系统升级后，该值会更新到升级之后的时间戳
* layoutVersion：HDFS 元数据格式的版本，HDFS 升级时会进行更新
* seen-txid：
    * 上一次 checkpoint（元数据合并）时的最后一个事务 id
    * NameNode 在启动时会检查 seen-txid 文件，以验证它至少可以加载该数目的事务。如果无法验证加载事务，NameNode 会中止启动

![图片alt](../images/metadata_id.PNG)

#### 查看 fsimage 和 edits log
oiv 可将 fsimage 文件的内容转化为可读形式：
> 常用命令：hdfs oiv -i fsimage_0000000000000050 -p XML -o fsimage.xml

oev 可查看 edits log 文件：
> 常用命令：hdfs oev -i edits_0000000000000050 -o edits.xml

## SecondaryNameNode
#### 背景
当 HDFS 集群运行一段时间后，就会出现以下问题：
1. edits log 文件越来越大
2. fsimage 距离上次持久化过了一段时间，导致文件不是最新的
3. 

























