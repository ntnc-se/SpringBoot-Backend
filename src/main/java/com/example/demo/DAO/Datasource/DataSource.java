package com.example.demo.DAO.Datasource;

import com.example.demo.Config.Settings;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Component;

@Component
public class DataSource {
    private static final Settings settings = Settings.getInstance();
    private static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    private static final String DB_URL = String.format("jdbc:mysql://%s:%s/%s", settings.MYSQL_IP, settings.MYSQL_PORT, settings.MYSQL_DB);
    private static final String DB_USER = settings.MYSQL_USER;
    private static final String DB_PASSWORD = settings.MYSQL_PASS;
    private static final int CONN_POOL_SIZE = settings.MYSQL_POOL_SIZE;

    private BasicDataSource bds = new BasicDataSource();

    private DataSource() {
        //Set database driver name
        bds.setDriverClassName(DRIVER_CLASS_NAME);
        //Set database url
        bds.setUrl(DB_URL);
        //Set database user
        bds.setUsername(DB_USER);
        //Set database password
        bds.setPassword(DB_PASSWORD);
        //Set the connection pool size
        bds.setInitialSize(CONN_POOL_SIZE);

        bds.setMaxTotal(CONN_POOL_SIZE);

        bds.setDefaultQueryTimeout(10);

        bds.setPoolPreparedStatements(true);
    }

    private static class DataSourceHolder {
        private static final DataSource INSTANCE = new DataSource();
    }

    public static DataSource getInstance() {
        return DataSourceHolder.INSTANCE;
    }

    public BasicDataSource getBds() {
        return bds;
    }

    public void setBds(BasicDataSource bds) {
        this.bds = bds;
    }
}
