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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DocumentService {

    @Autowired
    private DocumentMainInformationRepository repositoryMainInformation;

    /**
     * 本地信息同步策略接口
     */
    private interface StrategyLocalInformationSync extends Runnable {
    }

    /**
     * 本地信息同步策略 - 创建本地信息
     */
    private class CreateLocalInformation implements StrategyLocalInformationSync {
        @Override
        public void run() {
            try {
                if (mainInformation != null) {
                    if (mainInformation.getDocumentStatus() == DocumentModel.DocumentStatus.WAIT_FOR_CREATE) {//状态确认为等待创建
                        List<DocumentModel.DocumentMainInformation> list = repositoryMainInformation.findByUuid(mainInformation.getUuid());
                        if (list.isEmpty()) {//如果没有记录(完全为新的文档加入)
                            repositoryMainInformation.save(mainInformation);//保存到数据库
                        }
                        else {//如果有记录(文档从推迟创建恢复,重创建)
                            if(list.size() == 1){//确保要操作的条目是唯一的
                                mainInformation.setId(list.get(0).getId());//确保主键ID的对应或者确定主键ID(有的时候完全依靠UUID确定操作的条目,并不知道它的ID)
                                repositoryMainInformation.save(mainInformation);//保存到数据库
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error("CreateLocalInformation无法完成,因为:", e);
                throw e;
            }
        }

        DocumentModel.DocumentMainInformation mainInformation;

        public CreateLocalInformation(@Nonnull DocumentModel.DocumentMainInformation mainInformation) {
            this.mainInformation = mainInformation;
        }
    }

    /**
     * 本地信息同步策略 - 更新本地信息
     */
    private class UpdateLocalInformation implements StrategyLocalInformationSync {
        @Override
        public void run() {
            try {
                if (mainInformation != null) {
                    if(mainInformation.getDocumentStatus() == DocumentModel.DocumentStatus.WAIT_FOR_UPDATE){//状态确认为等待更新
                        List<DocumentModel.DocumentMainInformation> list = repositoryMainInformation.findByUuid(mainInformation.getUuid());
                        if (list.size() == 1) {//确保要操作的条目是唯一的
                            mainInformation.setId(list.get(0).getId());//确保主键ID的对应或者确定主键ID(有的时候完全依靠UUID确定操作的条目,并不知道它的ID)
                            repositoryMainInformation.save(mainInformation);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("UpdateLocalInformation无法完成,因为:", e);
                throw e;
            }
        }

        DocumentModel.DocumentMainInformation mainInformation;

        public UpdateLocalInformation(@Nonnull DocumentModel.DocumentMainInformation mainInformation) {
            this.mainInformation = mainInformation;
        }
    }


    /**
     * 本地文件同步策略接口
     */
    private interface StrategyLocalFileSync extends Runnable {
    }

    /**
     * 本地文件同步策略 - 创建本地文件
     */
    private class CreateLocalFile implements StrategyLocalFileSync {
        @Override
        public void run() {

        }
    }

    /**
     * 本地文件同步策略 - 更新本地文件
     */
    private class UpdateLocalFile implements StrategyLocalFileSync {
        @Override
        public void run() {

        }
    }

    /**
     * 本地文件同步策略 - 下线本地文件
     */
    private class OfflineLocalFile implements StrategyLocalFileSync {
        @Override
        public void run() {

        }
    }


    private static void localInformationSync() {

    }

    private static void localFileSync() {

    }

    public void localSync() {

    }


    public DocumentMainInformationRepository getRepositoryMainInformation() {
        return repositoryMainInformation;
    }
}
