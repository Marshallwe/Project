create table address
(
    id           bigint auto_increment comment 'Primary key'
        primary key,
    link_user    varchar(255) null comment 'Contact person',
    link_address varchar(255) null comment 'Address',
    link_phone   varchar(255) null comment 'Phone number',
    user_id      bigint       null comment 'User ID'
)
    comment 'Address table';

create table avatar
(
    id   bigint auto_increment comment 'Primary key'
        primary key,
    type varchar(255) null comment 'File type (extension, e.g., jpg)',
    size bigint       null comment 'File size',
    url  varchar(255) null comment 'File path',
    md5  varchar(255) null comment 'MD5 hash'
)
    comment 'Avatar table';

create table carousel
(
    id         bigint auto_increment comment 'Primary key'
        primary key,
    good_id    bigint null comment 'Product ID',
    show_order int    null comment 'Display order'
)
    comment 'Carousel table';

create table cart
(
    id          bigint auto_increment comment 'Primary key'
        primary key,
    count       int          null comment 'Quantity',
    create_time datetime     null comment 'Added time',
    good_id     bigint       null comment 'Product ID',
    standard    varchar(255) null comment 'Product specification',
    user_id     bigint       null comment 'User ID'
)
    comment 'Shopping cart table';

create table category
(
    id   bigint auto_increment comment 'Primary key'
        primary key,
    name varchar(255) null comment 'Category name'
)
    comment 'Category table';

create table good
(
    id          bigint auto_increment comment 'Primary key'
        primary key,
    name        varchar(255)                               null comment 'Product name',
    description varchar(1600)                              null comment 'Description',
    discount    double(10, 2) default 1.00                 null comment 'Discount rate',
    sales       bigint        default 0                    null comment 'Sales volume',
    sale_money  double(10, 2) default 0.00                 null comment 'Sales amount',
    category_id bigint                                     null comment 'Category ID',
    imgs        varchar(255)                               null comment 'Product images',
    create_time datetime(6)   default CURRENT_TIMESTAMP(6) null comment 'Creation time',
    recommend   tinyint(1)    default 0                    null comment 'Recommendation flag (0: No, 1: Yes)',
    is_delete   tinyint(1)    default 0                    null comment 'Deletion flag (0: Active, 1: Deleted)'
)
    comment 'Product table';

create table good_standard
(
    good_id bigint         null comment 'Product ID',
    value   varchar(255)   null comment 'Specification',
    price   decimal(10, 2) null comment 'Price',
    store   bigint         null comment 'Stock'
)
    comment 'Product specifications table';

create table icon
(
    id    bigint auto_increment comment 'Primary key'
        primary key,
    value varchar(255) null comment 'Icon identifier'
)
    comment 'Icon table';

create table icon_category
(
    category_id bigint not null comment 'Category ID'
        primary key,
    icon_id     bigint not null comment 'Icon ID'
)
    comment 'Category-Icon relationship table';

create table message
(
    id      int auto_increment comment 'Message ID'
        primary key,
    title   varchar(20)  null comment 'Title',
    content varchar(255) null comment 'Content',
    time    date         null comment 'Message time',
    good_id bigint       null comment 'Product ID'
)
    comment 'Message table';

create table order_goods
(
    id       bigint auto_increment comment 'Primary key'
        primary key,
    order_id bigint        null comment 'Order ID',
    good_id  bigint        null comment 'Product ID',
    count    int           null comment 'Quantity',
    standard varchar(1600) null comment 'Specification'
);

create table replay
(
    messageId  int          null comment 'Message ID',
    replayId   int auto_increment comment 'Reply ID'
        primary key,
    replay     varchar(255) null comment 'Content',
    replayTime date         null comment 'Reply time'
)
    comment 'Reply table';

create table standard
(
    goodId bigint         not null comment 'Product ID'
        primary key,
    value  varchar(255)   null comment 'Specification',
    price  decimal(10, 2) null comment 'Price',
    store  bigint         null comment 'Stock'
)
    comment 'Specifications table';

create table sys_file
(
    id        bigint auto_increment comment 'Primary key'
        primary key,
    name      varchar(255) null comment 'File name',
    type      varchar(255) null comment 'File type',
    size      bigint       null comment 'File size',
    url       varchar(255) null comment 'File path',
    is_delete tinyint(1)   null comment 'Is deleted (0: No, 1: Yes)',
    enable    tinyint(1)   null comment 'Is enabled (0: No, 1: Yes)',
    md5       varchar(255) null comment 'MD5 hash'
)
    comment 'System files table';

create table sys_user
(
    id         bigint auto_increment comment 'Primary key'
        primary key,
    username   varchar(255)  null comment 'Username',
    password   varchar(255)  null comment 'Password',
    nickname   varchar(255)  null comment 'Nickname',
    email      varchar(255)  null comment 'Email',
    phone      varchar(255)  null comment 'Phone number',
    address    varchar(1600) null comment 'Address',
    avatar_url varchar(255)  null comment 'Avatar URL',
    role       varchar(255)  null comment 'User role'
)
    comment 'User table';

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
    comment 'Orders table';


