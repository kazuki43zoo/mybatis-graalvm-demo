package com.example;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

import com.example.domain.City;
import com.example.mapper.CityMapper;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class Application {

  private static final Log log;

  static {
    LogFactory.useCustomLogging(StdOutImpl.class);
    log = LogFactory.getLog(Application.class);
  }

  public static void main(String... args) throws SQLException, IOException, NoSuchMethodException {
    log.debug(LocalDateTime.now() + ": initialize configuration...");
    Configuration configuration = new XMLConfigBuilder(Resources.getResourceAsReader("mybatis-config.xml")).parse();

    log.debug(LocalDateTime.now() + ": initialize data source...");
    initDataSource(configuration.getEnvironment().getDataSource());

    log.debug(LocalDateTime.now() + ": building sql session factory...");
    SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(configuration);

    log.debug(LocalDateTime.now() + ": open new session...");
    try (SqlSession sqlSession = factory.openSession()) {
      CityMapper cityMapper = sqlSession.getMapper(CityMapper.class);

      City newCity = new City();
      newCity.setName("Toshima-ku");
      newCity.setState("Tokyo");
      newCity.setCountry("JP");

      cityMapper.insert(newCity);

      {
        City loadedCity = cityMapper.findById(newCity.getId());
        log.debug(loadedCity.toString());
      }

      log.debug(LocalDateTime.now() + ": commit session...");
      sqlSession.commit();
    }
  }

  private static void initDataSource(DataSource dataSource) throws SQLException, IOException {
    ScriptRunner runner = new ScriptRunner(dataSource.getConnection());
    runner.runScript(Resources.getResourceAsReader("initDb.sql"));
  }

}
