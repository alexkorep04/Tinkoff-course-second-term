package edu.java.configuration;

import javax.sql.DataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig {
    @Autowired
    private DataSource dataSource;

    @Bean
    public DSLContext dslContext() {
        return DSL.using(dataSource, SQLDialect.POSTGRES);
    }

    @Bean
    public DefaultConfigurationCustomizer postgresJooqCustomizer() {
        return (DefaultConfiguration c) -> c.settings()
            .withRenderSchema(false)
            .withRenderFormatted(true)
            .withRenderQuotedNames(RenderQuotedNames.NEVER);
    }
}
