CREATE TABLE if not exists project (
  name varchar(255) NOT NULL COMMENT '项目名',
  jar_paths varchar(4096) NOT NULL COMMENT '项目jar包路径',
  PRIMARY KEY (name),
  INDEX idx_pj_name(name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='项目表';