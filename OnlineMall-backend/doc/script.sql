create table address
(
    THREAD_ID               bigint unsigned                                    not null,
    id                      bigint comment 'Primary key',
    EVENT_ID                bigint unsigned                                    not null,
    link_user               varchar(255)                                       null comment 'Contact person',
    END_EVENT_ID            bigint unsigned                                    null,
    link_address            varchar(255)                                       null comment 'Address',
    EVENT_NAME              varchar(128)                                       not null,
    link_phone              varchar(255)                                       null comment 'Phone number',
    SOURCE                  varchar(64)                                        null,
    user_id                 bigint                                             null comment 'User ID',
    TIMER_START             bigint unsigned                                    null,
    TIMER_END               bigint unsigned                                    null,
    TIMER_WAIT              bigint unsigned                                    null,
    LOCK_TIME               bigint unsigned                                    not null,
    SQL_TEXT                longtext                                           null,
    DIGEST                  varchar(64)                                        null,
    DIGEST_TEXT             longtext                                           null,
    CURRENT_SCHEMA          varchar(64)                                        null,
    OBJECT_TYPE             varchar(64)                                        null,
    OBJECT_SCHEMA           varchar(64)                                        null,
    OBJECT_NAME             varchar(64)                                        null,
    OBJECT_INSTANCE_BEGIN   bigint unsigned                                    null,
    MYSQL_ERRNO             int                                                null,
    RETURNED_SQLSTATE       varchar(5)                                         null,
    MESSAGE_TEXT            varchar(128)                                       null,
    ERRORS                  bigint unsigned                                    not null,
    WARNINGS                bigint unsigned                                    not null,
    ROWS_AFFECTED           bigint unsigned                                    not null,
    ROWS_SENT               bigint unsigned                                    not null,
    ROWS_EXAMINED           bigint unsigned                                    not null,
    CREATED_TMP_DISK_TABLES bigint unsigned                                    not null,
    CREATED_TMP_TABLES      bigint unsigned                                    not null,
    SELECT_FULL_JOIN        bigint unsigned                                    not null,
    SELECT_FULL_RANGE_JOIN  bigint unsigned                                    not null,
    SELECT_RANGE            bigint unsigned                                    not null,
    SELECT_RANGE_CHECK      bigint unsigned                                    not null,
    SELECT_SCAN             bigint unsigned                                    not null,
    SORT_MERGE_PASSES       bigint unsigned                                    not null,
    SORT_RANGE              bigint unsigned                                    not null,
    SORT_ROWS               bigint unsigned                                    not null,
    SORT_SCAN               bigint unsigned                                    not null,
    NO_INDEX_USED           bigint unsigned                                    not null,
    NO_GOOD_INDEX_USED      bigint unsigned                                    not null,
    NESTING_EVENT_ID        bigint unsigned                                    null,
    NESTING_EVENT_TYPE      enum ('TRANSACTION', 'STATEMENT', 'STAGE', 'WAIT') null,
    NESTING_EVENT_LEVEL     int                                                null,
    STATEMENT_ID            bigint unsigned                                    null,
    CPU_TIME                bigint unsigned                                    not null,
    MAX_CONTROLLED_MEMORY   bigint unsigned                                    not null,
    MAX_TOTAL_MEMORY        bigint unsigned                                    not null,
    EXECUTION_ENGINE        enum ('PRIMARY', 'SECONDARY')                      null,
    primary key (THREAD_ID, EVENT_ID) using hash,
    primary key (THREAD_ID, EVENT_ID) using hash
)
    comment 'Address table' engine = InnoDB;

create unique index `PRIMARY`
    on address (id);

alter table address
    modify id bigint auto_increment comment 'Primary key';

create table avatar
(
    id   bigint auto_increment comment 'Primary key'
        primary key,
    type varchar(255) null comment 'File type (extension, e.g., jpg)',
    size bigint       null comment 'File size',
    url  varchar(255) null comment 'File path',
    md5  varchar(255) null comment 'MD5 hash'
)
    comment 'Avatar table' engine = InnoDB;

create table carousel
(
    THREAD_ID               bigint unsigned                                    not null,
    id                      bigint auto_increment comment 'Primary key'
        primary key,
    EVENT_ID                bigint unsigned                                    not null,
    good_id                 bigint                                             null comment 'Product ID',
    END_EVENT_ID            bigint unsigned                                    null,
    show_order              int                                                null comment 'Display order',
    EVENT_NAME              varchar(128)                                       not null,
    SOURCE                  varchar(64)                                        null,
    TIMER_START             bigint unsigned                                    null,
    TIMER_END               bigint unsigned                                    null,
    TIMER_WAIT              bigint unsigned                                    null,
    LOCK_TIME               bigint unsigned                                    not null,
    SQL_TEXT                longtext                                           null,
    DIGEST                  varchar(64)                                        null,
    DIGEST_TEXT             longtext                                           null,
    CURRENT_SCHEMA          varchar(64)                                        null,
    OBJECT_TYPE             varchar(64)                                        null,
    OBJECT_SCHEMA           varchar(64)                                        null,
    OBJECT_NAME             varchar(64)                                        null,
    OBJECT_INSTANCE_BEGIN   bigint unsigned                                    null,
    MYSQL_ERRNO             int                                                null,
    RETURNED_SQLSTATE       varchar(5)                                         null,
    MESSAGE_TEXT            varchar(128)                                       null,
    ERRORS                  bigint unsigned                                    not null,
    WARNINGS                bigint unsigned                                    not null,
    ROWS_AFFECTED           bigint unsigned                                    not null,
    ROWS_SENT               bigint unsigned                                    not null,
    ROWS_EXAMINED           bigint unsigned                                    not null,
    CREATED_TMP_DISK_TABLES bigint unsigned                                    not null,
    CREATED_TMP_TABLES      bigint unsigned                                    not null,
    SELECT_FULL_JOIN        bigint unsigned                                    not null,
    SELECT_FULL_RANGE_JOIN  bigint unsigned                                    not null,
    SELECT_RANGE            bigint unsigned                                    not null,
    SELECT_RANGE_CHECK      bigint unsigned                                    not null,
    SELECT_SCAN             bigint unsigned                                    not null,
    SORT_MERGE_PASSES       bigint unsigned                                    not null,
    SORT_RANGE              bigint unsigned                                    not null,
    SORT_ROWS               bigint unsigned                                    not null,
    SORT_SCAN               bigint unsigned                                    not null,
    NO_INDEX_USED           bigint unsigned                                    not null,
    NO_GOOD_INDEX_USED      bigint unsigned                                    not null,
    NESTING_EVENT_ID        bigint unsigned                                    null,
    NESTING_EVENT_TYPE      enum ('TRANSACTION', 'STATEMENT', 'STAGE', 'WAIT') null,
    NESTING_EVENT_LEVEL     int                                                null,
    STATEMENT_ID            bigint unsigned                                    null,
    CPU_TIME                bigint unsigned                                    not null,
    MAX_CONTROLLED_MEMORY   bigint unsigned                                    not null,
    MAX_TOTAL_MEMORY        bigint unsigned                                    not null,
    EXECUTION_ENGINE        enum ('PRIMARY', 'SECONDARY')                      null
)
    comment 'Carousel table' engine = InnoDB;

create table cart
(
    USER                        char(32) collate utf8mb4_bin null,
    id                          bigint auto_increment comment 'Primary key'
        primary key,
    HOST                        char(255) charset ascii      null,
    count                       int                          null comment 'Quantity',
    EVENT_NAME                  varchar(128)                 not null,
    create_time                 datetime                     null comment 'Added time',
    COUNT_STAR                  bigint unsigned              not null,
    good_id                     bigint                       null comment 'Product ID',
    SUM_TIMER_WAIT              bigint unsigned              not null,
    standard                    varchar(255)                 null comment 'Product specification',
    MIN_TIMER_WAIT              bigint unsigned              not null,
    user_id                     bigint                       null comment 'User ID',
    AVG_TIMER_WAIT              bigint unsigned              not null,
    MAX_TIMER_WAIT              bigint unsigned              not null,
    SUM_LOCK_TIME               bigint unsigned              not null,
    SUM_ERRORS                  bigint unsigned              not null,
    SUM_WARNINGS                bigint unsigned              not null,
    SUM_ROWS_AFFECTED           bigint unsigned              not null,
    SUM_ROWS_SENT               bigint unsigned              not null,
    SUM_ROWS_EXAMINED           bigint unsigned              not null,
    SUM_CREATED_TMP_DISK_TABLES bigint unsigned              not null,
    SUM_CREATED_TMP_TABLES      bigint unsigned              not null,
    SUM_SELECT_FULL_JOIN        bigint unsigned              not null,
    SUM_SELECT_FULL_RANGE_JOIN  bigint unsigned              not null,
    SUM_SELECT_RANGE            bigint unsigned              not null,
    SUM_SELECT_RANGE_CHECK      bigint unsigned              not null,
    SUM_SELECT_SCAN             bigint unsigned              not null,
    SUM_SORT_MERGE_PASSES       bigint unsigned              not null,
    SUM_SORT_RANGE              bigint unsigned              not null,
    SUM_SORT_ROWS               bigint unsigned              not null,
    SUM_SORT_SCAN               bigint unsigned              not null,
    SUM_NO_INDEX_USED           bigint unsigned              not null,
    SUM_NO_GOOD_INDEX_USED      bigint unsigned              not null,
    SUM_CPU_TIME                bigint unsigned              not null,
    MAX_CONTROLLED_MEMORY       bigint unsigned              not null,
    MAX_TOTAL_MEMORY            bigint unsigned              not null,
    COUNT_SECONDARY             bigint unsigned              not null
)
    comment 'Shopping cart table' engine = InnoDB;

create unique index ACCOUNT
    on cart (USER, HOST, EVENT_NAME)
    using hash;

create table category
(
    id   bigint auto_increment comment 'Primary key'
        primary key,
    name varchar(255) null comment 'Category name'
)
    comment 'Category table' engine = InnoDB;

create table good
(
    SCHEMA_NAME                 varchar(64)                                null,
    id                          bigint auto_increment comment 'Primary key'
        primary key,
    DIGEST                      varchar(64)                                null,
    name                        varchar(255)                               null comment 'Product name',
    DIGEST_TEXT                 longtext                                   null,
    description                 varchar(1600)                              null comment 'Description',
    COUNT_STAR                  bigint unsigned                            not null,
    discount                    double(10, 2) default 1.00                 null comment 'Discount rate',
    SUM_TIMER_WAIT              bigint unsigned                            not null,
    sales                       bigint        default 0                    null comment 'Sales volume',
    MIN_TIMER_WAIT              bigint unsigned                            not null,
    sale_money                  double(10, 2) default 0.00                 null comment 'Sales amount',
    AVG_TIMER_WAIT              bigint unsigned                            not null,
    category_id                 bigint                                     null comment 'Category ID',
    MAX_TIMER_WAIT              bigint unsigned                            not null,
    imgs                        varchar(255)                               null comment 'Product images',
    SUM_LOCK_TIME               bigint unsigned                            not null,
    create_time                 datetime(6)   default CURRENT_TIMESTAMP(6) null comment 'Creation time',
    SUM_ERRORS                  bigint unsigned                            not null,
    recommend                   tinyint(1)    default 0                    null comment 'Recommendation flag (0: No, 1: Yes)',
    SUM_WARNINGS                bigint unsigned                            not null,
    is_delete                   tinyint(1)    default 0                    null comment 'Deletion flag (0: Active, 1: Deleted)',
    SUM_ROWS_AFFECTED           bigint unsigned                            not null,
    SUM_ROWS_SENT               bigint unsigned                            not null,
    SUM_ROWS_EXAMINED           bigint unsigned                            not null,
    SUM_CREATED_TMP_DISK_TABLES bigint unsigned                            not null,
    SUM_CREATED_TMP_TABLES      bigint unsigned                            not null,
    SUM_SELECT_FULL_JOIN        bigint unsigned                            not null,
    SUM_SELECT_FULL_RANGE_JOIN  bigint unsigned                            not null,
    SUM_SELECT_RANGE            bigint unsigned                            not null,
    SUM_SELECT_RANGE_CHECK      bigint unsigned                            not null,
    SUM_SELECT_SCAN             bigint unsigned                            not null,
    SUM_SORT_MERGE_PASSES       bigint unsigned                            not null,
    SUM_SORT_RANGE              bigint unsigned                            not null,
    SUM_SORT_ROWS               bigint unsigned                            not null,
    SUM_SORT_SCAN               bigint unsigned                            not null,
    SUM_NO_INDEX_USED           bigint unsigned                            not null,
    SUM_NO_GOOD_INDEX_USED      bigint unsigned                            not null,
    SUM_CPU_TIME                bigint unsigned                            not null,
    MAX_CONTROLLED_MEMORY       bigint unsigned                            not null,
    MAX_TOTAL_MEMORY            bigint unsigned                            not null,
    COUNT_SECONDARY             bigint unsigned                            not null,
    FIRST_SEEN                  timestamp(6)                               not null,
    LAST_SEEN                   timestamp(6)                               not null,
    QUANTILE_95                 bigint unsigned                            not null,
    QUANTILE_99                 bigint unsigned                            not null,
    QUANTILE_999                bigint unsigned                            not null,
    QUERY_SAMPLE_TEXT           longtext                                   null,
    QUERY_SAMPLE_SEEN           timestamp(6)                               not null,
    QUERY_SAMPLE_TIMER_WAIT     bigint unsigned                            not null
)
    comment 'Product table' engine = InnoDB;

create unique index SCHEMA_NAME
    on good (SCHEMA_NAME, DIGEST)
    using hash;

create table good_standard
(
    good_id bigint         null comment 'Product ID',
    value   varchar(255)   null comment 'Specification',
    price   decimal(10, 2) null comment 'Price',
    store   bigint         null comment 'Stock'
)
    comment 'Product specifications table' engine = InnoDB;

create table icon
(
    HOST                        char(255) charset ascii null,
    id                          bigint auto_increment comment 'Primary key'
        primary key,
    EVENT_NAME                  varchar(128)            not null,
    value                       varchar(255)            null comment 'Icon identifier',
    COUNT_STAR                  bigint unsigned         not null,
    SUM_TIMER_WAIT              bigint unsigned         not null,
    MIN_TIMER_WAIT              bigint unsigned         not null,
    AVG_TIMER_WAIT              bigint unsigned         not null,
    MAX_TIMER_WAIT              bigint unsigned         not null,
    SUM_LOCK_TIME               bigint unsigned         not null,
    SUM_ERRORS                  bigint unsigned         not null,
    SUM_WARNINGS                bigint unsigned         not null,
    SUM_ROWS_AFFECTED           bigint unsigned         not null,
    SUM_ROWS_SENT               bigint unsigned         not null,
    SUM_ROWS_EXAMINED           bigint unsigned         not null,
    SUM_CREATED_TMP_DISK_TABLES bigint unsigned         not null,
    SUM_CREATED_TMP_TABLES      bigint unsigned         not null,
    SUM_SELECT_FULL_JOIN        bigint unsigned         not null,
    SUM_SELECT_FULL_RANGE_JOIN  bigint unsigned         not null,
    SUM_SELECT_RANGE            bigint unsigned         not null,
    SUM_SELECT_RANGE_CHECK      bigint unsigned         not null,
    SUM_SELECT_SCAN             bigint unsigned         not null,
    SUM_SORT_MERGE_PASSES       bigint unsigned         not null,
    SUM_SORT_RANGE              bigint unsigned         not null,
    SUM_SORT_ROWS               bigint unsigned         not null,
    SUM_SORT_SCAN               bigint unsigned         not null,
    SUM_NO_INDEX_USED           bigint unsigned         not null,
    SUM_NO_GOOD_INDEX_USED      bigint unsigned         not null,
    SUM_CPU_TIME                bigint unsigned         not null,
    MAX_CONTROLLED_MEMORY       bigint unsigned         not null,
    MAX_TOTAL_MEMORY            bigint unsigned         not null,
    COUNT_SECONDARY             bigint unsigned         not null
)
    comment 'Icon table' engine = InnoDB;

create unique index HOST
    on icon (HOST, EVENT_NAME)
    using hash;

create table icon_category
(
    category_id bigint not null comment 'Category ID'
        primary key,
    icon_id     bigint not null comment 'Icon ID'
)
    comment 'Category-Icon relationship table' engine = InnoDB;

create table message
(
    OBJECT_TYPE                 enum ('EVENT', 'FUNCTION', 'PROCEDURE', 'TABLE', 'TRIGGER') not null,
    id                          int comment 'Message ID',
    OBJECT_SCHEMA               varchar(64)                                                 not null,
    title                       varchar(20)                                                 null comment 'Title',
    OBJECT_NAME                 varchar(64)                                                 not null,
    content                     varchar(255)                                                null comment 'Content',
    COUNT_STAR                  bigint unsigned                                             not null,
    time                        date                                                        null comment 'Message time',
    SUM_TIMER_WAIT              bigint unsigned                                             not null,
    good_id                     bigint                                                      null comment 'Product ID',
    MIN_TIMER_WAIT              bigint unsigned                                             not null,
    AVG_TIMER_WAIT              bigint unsigned                                             not null,
    MAX_TIMER_WAIT              bigint unsigned                                             not null,
    COUNT_STATEMENTS            bigint unsigned                                             not null,
    SUM_STATEMENTS_WAIT         bigint unsigned                                             not null,
    MIN_STATEMENTS_WAIT         bigint unsigned                                             not null,
    AVG_STATEMENTS_WAIT         bigint unsigned                                             not null,
    MAX_STATEMENTS_WAIT         bigint unsigned                                             not null,
    SUM_LOCK_TIME               bigint unsigned                                             not null,
    SUM_ERRORS                  bigint unsigned                                             not null,
    SUM_WARNINGS                bigint unsigned                                             not null,
    SUM_ROWS_AFFECTED           bigint unsigned                                             not null,
    SUM_ROWS_SENT               bigint unsigned                                             not null,
    SUM_ROWS_EXAMINED           bigint unsigned                                             not null,
    SUM_CREATED_TMP_DISK_TABLES bigint unsigned                                             not null,
    SUM_CREATED_TMP_TABLES      bigint unsigned                                             not null,
    SUM_SELECT_FULL_JOIN        bigint unsigned                                             not null,
    SUM_SELECT_FULL_RANGE_JOIN  bigint unsigned                                             not null,
    SUM_SELECT_RANGE            bigint unsigned                                             not null,
    SUM_SELECT_RANGE_CHECK      bigint unsigned                                             not null,
    SUM_SELECT_SCAN             bigint unsigned                                             not null,
    SUM_SORT_MERGE_PASSES       bigint unsigned                                             not null,
    SUM_SORT_RANGE              bigint unsigned                                             not null,
    SUM_SORT_ROWS               bigint unsigned                                             not null,
    SUM_SORT_SCAN               bigint unsigned                                             not null,
    SUM_NO_INDEX_USED           bigint unsigned                                             not null,
    SUM_NO_GOOD_INDEX_USED      bigint unsigned                                             not null,
    SUM_CPU_TIME                bigint unsigned                                             not null,
    MAX_CONTROLLED_MEMORY       bigint unsigned                                             not null,
    MAX_TOTAL_MEMORY            bigint unsigned                                             not null,
    COUNT_SECONDARY             bigint unsigned                                             not null,
    primary key (OBJECT_TYPE, OBJECT_SCHEMA, OBJECT_NAME) using hash,
    primary key (OBJECT_TYPE, OBJECT_SCHEMA, OBJECT_NAME) using hash
)
    comment 'Message table' engine = InnoDB;

create unique index `PRIMARY`
    on message (id);

alter table message
    modify id int auto_increment comment 'Message ID';

create table order_goods
(
    id       bigint auto_increment comment 'Primary key'
        primary key,
    order_id bigint        null comment 'Order ID',
    good_id  bigint        null comment 'Product ID',
    count    int           null comment 'Quantity',
    standard varchar(1600) null comment 'Specification'
)
    engine = InnoDB;

create table replay
(
    THREAD_ID                   bigint unsigned not null,
    messageId                   int             null comment 'Message ID',
    EVENT_NAME                  varchar(128)    not null,
    replayId                    int comment 'Reply ID',
    COUNT_STAR                  bigint unsigned not null,
    replay                      varchar(255)    null comment 'Content',
    SUM_TIMER_WAIT              bigint unsigned not null,
    replayTime                  date            null comment 'Reply time',
    MIN_TIMER_WAIT              bigint unsigned not null,
    AVG_TIMER_WAIT              bigint unsigned not null,
    MAX_TIMER_WAIT              bigint unsigned not null,
    SUM_LOCK_TIME               bigint unsigned not null,
    SUM_ERRORS                  bigint unsigned not null,
    SUM_WARNINGS                bigint unsigned not null,
    SUM_ROWS_AFFECTED           bigint unsigned not null,
    SUM_ROWS_SENT               bigint unsigned not null,
    SUM_ROWS_EXAMINED           bigint unsigned not null,
    SUM_CREATED_TMP_DISK_TABLES bigint unsigned not null,
    SUM_CREATED_TMP_TABLES      bigint unsigned not null,
    SUM_SELECT_FULL_JOIN        bigint unsigned not null,
    SUM_SELECT_FULL_RANGE_JOIN  bigint unsigned not null,
    SUM_SELECT_RANGE            bigint unsigned not null,
    SUM_SELECT_RANGE_CHECK      bigint unsigned not null,
    SUM_SELECT_SCAN             bigint unsigned not null,
    SUM_SORT_MERGE_PASSES       bigint unsigned not null,
    SUM_SORT_RANGE              bigint unsigned not null,
    SUM_SORT_ROWS               bigint unsigned not null,
    SUM_SORT_SCAN               bigint unsigned not null,
    SUM_NO_INDEX_USED           bigint unsigned not null,
    SUM_NO_GOOD_INDEX_USED      bigint unsigned not null,
    SUM_CPU_TIME                bigint unsigned not null,
    MAX_CONTROLLED_MEMORY       bigint unsigned not null,
    MAX_TOTAL_MEMORY            bigint unsigned not null,
    COUNT_SECONDARY             bigint unsigned not null,
    primary key (THREAD_ID, EVENT_NAME) using hash,
    primary key (THREAD_ID, EVENT_NAME) using hash
)
    comment 'Reply table' engine = InnoDB;

create unique index `PRIMARY`
    on replay (replayId);

alter table replay
    modify replayId int auto_increment comment 'Reply ID';

create table standard
(
    goodId bigint         not null comment 'Product ID'
        primary key,
    value  varchar(255)   null comment 'Specification',
    price  decimal(10, 2) null comment 'Price',
    store  bigint         null comment 'Stock'
)
    comment 'Specifications table' engine = InnoDB;

create table sys_file
(
    EVENT_NAME            varchar(128)    not null,
    id                    bigint auto_increment comment 'Primary key'
        primary key
        primary key,
    OBJECT_INSTANCE_BEGIN bigint unsigned not null,
    name                  varchar(255)    null comment 'File name',
    COUNT_STAR            bigint unsigned not null,
    type                  varchar(255)    null comment 'File type',
    SUM_TIMER_WAIT        bigint unsigned not null,
    size                  bigint          null comment 'File size',
    MIN_TIMER_WAIT        bigint unsigned not null,
    url                   varchar(255)    null comment 'File path',
    AVG_TIMER_WAIT        bigint unsigned not null,
    is_delete             tinyint(1)      null comment 'Is deleted (0: No, 1: Yes)',
    MAX_TIMER_WAIT        bigint unsigned not null,
    enable                tinyint(1)      null comment 'Is enabled (0: No, 1: Yes)',
    md5                   varchar(255)    null comment 'MD5 hash'
)
    comment 'System files table' engine = InnoDB;

create index EVENT_NAME
    on sys_file (EVENT_NAME)
    using hash;

create unique index `PRIMARY`
    on sys_file (OBJECT_INSTANCE_BEGIN)
    using hash;

create table sys_user
(
    EVENT_NAME     varchar(128)    not null,
    id             bigint auto_increment comment 'Primary key'
        primary key
        primary key,
    COUNT_STAR     bigint unsigned not null,
    username       varchar(255)    null comment 'Username',
    SUM_TIMER_WAIT bigint unsigned not null,
    password       varchar(255)    null comment 'Password',
    MIN_TIMER_WAIT bigint unsigned not null,
    nickname       varchar(255)    null comment 'Nickname',
    AVG_TIMER_WAIT bigint unsigned not null,
    email          varchar(255)    null comment 'Email',
    MAX_TIMER_WAIT bigint unsigned not null,
    phone          varchar(255)    null comment 'Phone number',
    address        varchar(1600)   null comment 'Address',
    avatar_url     varchar(255)    null comment 'Avatar URL',
    role           varchar(255)    null comment 'User role'
)
    comment 'User table' engine = InnoDB;

create unique index `PRIMARY`
    on sys_user (EVENT_NAME)
    using hash;

create table t_order
(
    id           bigint auto_increment comment 'Primary key'
        primary key,
    order_no     varchar(255)   null comment 'Order number',
    total_price  decimal(10, 2) null comment 'Total price',
    user_id      bigint         null comment 'User ID',
    link_user    varchar(255)   null comment 'Contact person',
    link_phone   varchar(255)   null comment 'Contact phone',
    link_address varchar(255)   null comment 'Delivery address',
    state        varchar(255)   null comment 'Order status',
    create_time  datetime       null comment 'Creation time'
)
    comment 'Orders table' engine = InnoDB;


