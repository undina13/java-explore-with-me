drop table if exists hits;

CREATE TABLE IF NOT EXISTS hits (
                                      id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                      app VARCHAR(512) NOT NULL,
                                      uri VARCHAR(512),
                                      ip VARCHAR(512),
                                      timestamp TIMESTAMP  NOT NULL,
                                      CONSTRAINT pk_hitss PRIMARY KEY (id)

);
