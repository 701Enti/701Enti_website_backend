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
//        SOFTWARE
package com.org701enti.repository;

import com.org701enti.model.DocumentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DocumentMainInformationRepository extends JpaRepository<DocumentModel.DocumentMainInformation,Long> {
    List<DocumentModel.DocumentMainInformation> findByName(String name);//通过文档名查询
    List<DocumentModel.DocumentMainInformation> findByUuid(String uuid);//通过唯一标识查询
    List<DocumentModel.DocumentMainInformation> findByAim(String aim);//通过项目名称查询
    List<DocumentModel.DocumentMainInformation> findByAuthor(String author);//通过作者/作者名单查询
    List<DocumentModel.DocumentMainInformation> findByBriefDescription(String briefDescription);//通过对文档的简介查询
    List<DocumentModel.DocumentMainInformation> findByUpdateTimeContent(LocalDateTime updateTimeContent);//通过文档内容最新更新时间查询
    List<DocumentModel.DocumentMainInformation> findByCreateTime(LocalDateTime createTime);//通过文档创建时间查询
    List<DocumentModel.DocumentMainInformation> findByUpdateTimeEntity(LocalDateTime updateTimeEntity);//通过文档对应的数据库实体最新更新时间查询
    List<DocumentModel.DocumentMainInformation> findByRoot(String root);//通过目录路径查询
    List<DocumentModel.DocumentMainInformation> findByFile(String file);//通过具体指向的文件名(含扩展名)查询
    List<DocumentModel.DocumentMainInformation> findByLogoURL(String logoURL);//通过文档logo的URL查询
}
