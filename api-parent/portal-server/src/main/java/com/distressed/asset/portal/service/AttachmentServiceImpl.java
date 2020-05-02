/*************************************************************************
 *                  HONGLING CAPITAL CONFIDENTIAL AND PROPRIETARY
 *
 *                COPYRIGHT (C) HONGLING CAPITAL CORPORATION 2012
 *    ALL RIGHTS RESERVED BY HONGLING CAPITAL CORPORATION. THIS PROGRAM
 * MUST BE USED  SOLELY FOR THE PURPOSE FOR WHICH IT WAS FURNISHED BY
 * HONGLING CAPITAL CORPORATION. NO PART OF THIS PROGRAM MAY BE REPRODUCED
 * OR DISCLOSED TO OTHERS,IN ANY FORM, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF HONGLING CAPITAL CORPORATION. USE OF COPYRIGHT NOTICE
 DOES NOT EVIDENCE PUBLICATION OF THE PROGRAM.
 *                  HONGLING CAPITAL CONFIDENTIAL AND PROPRIETARY
 *************************************************************************/

package com.distressed.asset.portal.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.distressed.asset.portal.dao.AttachmentDAO;
import com.distressed.asset.portal.dto.AttachmentDTO;
import com.distressed.asset.portal.mapping.Attachment;
import com.distressed.asset.portal.transform.AttachmentDT;
import com.distressed.asset.common.exception.RowsUpdateNotMatchException;
import com.distressed.asset.common.result.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 附件业务实现类。
 *
 * @author hongchao zhao at 2019-11-19 17:14
 */
@RestController
@CacheConfig(cacheNames = "attachmentCache")
public class AttachmentServiceImpl implements AttachmentService{

    private static Logger LOG = LoggerFactory.getLogger(AttachmentServiceImpl.class);

    @Resource
    private AttachmentDAO attachmentDAO;

    @Override
    public ResultBean<Long> saveOrUpdateAttachment(AttachmentDTO attachmentDTO) {
        Attachment attachment = new AttachmentDT().to(attachmentDTO);
        int count = 0;
        if (attachment.getId() == null) {
            count = attachmentDAO.insert(attachment);
            LOG.debug("新增附件后，返回附件编号【{}】。", attachment.getId());
        } else {
            count = attachmentDAO.updateByPrimaryKey(attachment);
        }
        if (count != 1) {
            throw new RowsUpdateNotMatchException("变更附件信息数据异常，附件数据：" + attachment.toString());
        }
        return ResultBean.successForData(attachment.getId());
    }

    @Override
    public ResultBean<AttachmentDTO> queryAttachmentById(Long attachmentId) {
        return ResultBean.successForData(new AttachmentDT().from(attachmentDAO.selectByPrimaryKey(attachmentId)));
    }

    @Override
    @Cacheable(keyGenerator = "cacheKeyGenerator",unless = "#result.data == null ")
    public ResultBean<AttachmentDTO> queryAttachmentByUuid(String uuid) {
        return ResultBean.successForData(new AttachmentDT().from(attachmentDAO.selectByUuid(uuid)));
    }

    @Override
    public PageInfo<AttachmentDTO> pageAttachmentList(int pageNum, int pageSize) {
        //设置分页开始
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<AttachmentDTO> list = new AttachmentDT().fromByList(attachmentDAO.selectAll());
        //4. 根据返回的集合，创建PageInfo对象
        PageInfo<AttachmentDTO> info = new PageInfo<>(list);
        info.setTotal(page.getTotal());
        return info;
    }

    @Override
    public ResultBean deleteAttachment(Long id) {
        int count= attachmentDAO.deleteByPrimaryKey(id);
        if (count !=1){
            throw new RowsUpdateNotMatchException("删除附件数据异常：" + id);
        }
        return ResultBean.success();
    }

    @Override
    public ResultBean<List<AttachmentDTO>> getAttachmentPassList() {
        List<AttachmentDTO> list=new AttachmentDT().fromByList(attachmentDAO.selectPass());
        return ResultBean.successForData(list);
    }

    @Override
    public ResultBean deleteAttachmentPass(Long id) {
        int count= attachmentDAO.deletePass(id);
        if(count<0)
        {
            throw new RowsUpdateNotMatchException("删除附件数据异常：");
        }
        return ResultBean.success();
    }
}
