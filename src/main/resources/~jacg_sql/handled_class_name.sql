CREATE TABLE if not exists handled_class_name_{appName} (
  full_name varchar(255) NOT NULL COMMENT '完整类名',
  simple_name varchar(255) NOT NULL COMMENT '唯一类名',
  PRIMARY KEY (full_name),
  INDEX idx_hcn_simple_name_{appName}(simple_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='处理过的类名信息表';