//        701Enti MIT License
//
//        Copyright (c) 2025 701Enti
//
//        Permission is hereby granted, free of charge, to any person obtaining a copy
//        of this software and associated documentation files (the "Software"), to deal
//        in the Software without restriction, including without limitation the rights
//        to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//        copies of the Software, and to permit persons to whom the Software is
//        furnished to do so, subject to the following conditions:
//
//        The above copyright notice and this permission notice shall be included in all
//        copies or substantial portions of the Software.
//
//        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//        IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//        FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//        AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//        LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//        OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//        SOFTWARE.
package com.org701enti.service;

import com.org701enti.model.DocumentModel;
import com.org701enti.repository.DocumentMainInformationRepository;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {

    @Autowired
    private DocumentMainInformationRepository mainInformationRepository;

    /**
     * 本地信息同步策略接口
     */
    private interface StrategyLocalInformationSync extends Runnable{
    }

    /**
     * 本地信息同步策略 - 创建本地信息
     */
    private class CreateLocalInformation implements StrategyLocalInformationSync{
        DocumentModel.DocumentMainInformation mainInformation;//实体数据缓存
        public CreateLocalInformation(@Nonnull DocumentModel.DocumentMainInformation mainInformation){
            this.mainInformation = mainInformation;
        }
        @Override
        public void run() {
            try{
                if (mainInformation != null){
                    if (!mainInformationRepository.findByUuid(mainInformation.getUuid()).isEmpty()){
                        mainInformationRepository.save(mainInformation);

                    }
                }
            }
            catch (NullPointerException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 本地信息同步策略 - 更新本地信息
     */
    private class UpdateLocalInformation implements StrategyLocalInformationSync{
        @Override
        public void run() {

        }
    }


    /**
     * 本地文件同步策略接口
     */
    private interface StrategyLocalFileSync extends Runnable{
    }

    /**
     * 本地文件同步策略 - 创建本地文件
     */
    private class CreateLocalFile implements StrategyLocalFileSync{
        @Override
        public void run() {

        }
    }

    /**
     * 本地文件同步策略 - 更新本地文件
     */
    private class UpdateLocalFile implements StrategyLocalFileSync{
        @Override
        public void run() {

        }
    }

    /**
     * 本地文件同步策略 - 下线本地文件
     */
    private class OfflineLocalFile implements StrategyLocalFileSync{
        @Override
        public void run() {

        }
    }


    private static void localInformationSync(){

    }

    private static void localFileSync(){

    }

    public void localSync(){

    }


}
