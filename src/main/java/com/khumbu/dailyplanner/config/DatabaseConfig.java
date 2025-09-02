package com.khumbu.dailyplanner.config;

import com.khumbu.dailyplanner.utils.EncryptionUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@Getter
@Setter
public class DatabaseConfig {

//return database config bean that does decryption first

    public DataSourceInfo dataSourceInfo(){
//initialize datasource
        DataSourceInfo dataSourceInfo=new  DataSourceInfo();
        //decrypt password
        //String password= EncryptionUtil.decrypt(dataSourceInfo.getPassword());
        //set the password
       // dataSourceInfo.setPassword(password);
        //return datasource info
       return dataSourceInfo;
    }
}
