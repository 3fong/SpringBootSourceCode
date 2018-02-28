package com.wisely.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisely.ui.domain.Person;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * @author liulei
 * @Description 消息转换
 * @create 2017/8/18 10:11
 */
public class MyMessageConverter extends AbstractHttpMessageConverter<Person> {
    // 声明转换媒体类型,不设置请求方法无法找到对应的转换方法
    public MyMessageConverter() {
        super(new MediaType("application", "json", Charset.forName("utf-8")));
    }

    @Override
    protected boolean supports(Class<?> aClass) {
        // 判断类型是否支持
        return Person.class.isAssignableFrom(aClass);
    }

    @Override
    protected Person readInternal(Class<? extends Person> aClass, HttpInputMessage httpInputMessage) throws
            IOException, HttpMessageNotReadableException {
        // 将流转为字符串
        String data = StreamUtils.copyToString(httpInputMessage.getBody(), Charset.forName("utf-8"));
        System.out.println("读入数据: " + data);
        ObjectMapper mapper = new ObjectMapper();
        Person person = mapper.readValue(data, Person.class);
        return person;
    }

    @Override
    protected void writeInternal(Person person, HttpOutputMessage httpOutputMessage) throws IOException,
            HttpMessageNotWritableException {
        ObjectMapper mapper = new ObjectMapper();
        String writeValueAsString = mapper.writeValueAsString(person);
        OutputStream body = httpOutputMessage.getBody();
        int index = 0;
        byte[] bytes = writeValueAsString.getBytes();
        body.write(bytes);
    }
}
