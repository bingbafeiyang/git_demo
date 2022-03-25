package com.hogwatsmini.demo.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2//启用swagger2
public class SwaggerConfig {//swagger 配置类的壳
    @Bean //
    public Docket docket(){//swagger自己的类，注入到SwaggerConfig中
        ParameterBuilder builder = new ParameterBuilder();
        builder.parameterType("header").name("token")
                .description("token值")
                .required(true)
                .modelRef(new ModelRef("String"));//swagger里显示header
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("aitest_interface")//接口名字
                .apiInfo(apiInfo())
                .globalOperationParameters(Lists.newArrayList(builder.build()))
                .select().paths(PathSelectors.any()).build();

    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("aitest-mini系统")
                .description("aitest-mini接口文档")
                .contact(new Contact("tlibn","","103@qq.com"))
                .version("1.0")
                .build();

    }
}
