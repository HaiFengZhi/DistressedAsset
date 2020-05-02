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

import com.github.pagehelper.PageInfo;
import com.distressed.asset.portal.dto.AttachmentDTO;
import com.distressed.asset.common.result.ResultBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 文件附件管理数据交互层。
 *
 * @author hongchao zhao at 2019-11-19 15:50
 */
@FeignClient(value = "portal-server", contextId = "attachmentService")
public interface AttachmentService {

    /**
     * 保存附件信息到数据库。
     *
     * @param attachmentDTO 附件信息。
     * @return 结果。
     */
    @PostMapping("/saveOrUpdateAttachment")
    ResultBean<Long> saveOrUpdateAttachment(@RequestBody AttachmentDTO attachmentDTO);

    /**
     * 根据附件编号获取附件详情。
     *
     * @param attachmentId 附件编号。
     * @return 附件详情。
     */
    @PostMapping("/queryAttachmentById")
    ResultBean<AttachmentDTO> queryAttachmentById(@RequestParam("attachmentId") Long attachmentId);

    /**
     * 根据uuid获取附件信息。
     *
     * @param uuid 附件唯一标识。
     * @return 附件信息。
     */
    @PostMapping("/queryAttachmentByUuid")
    ResultBean<AttachmentDTO> queryAttachmentByUuid(@RequestParam("uuid") String uuid);
    /**
     * 根据查询条件分页查询附件集合。
     * @param pageNum 当前页码。
     * @param pageSize 每页显示条数。
     * @return 用户帐号集合。
     */
    @PostMapping("/pageAttachmentList")
    PageInfo<AttachmentDTO> pageAttachmentList(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize);

    /**
     * 删除附件数据。
     *
     * @param id 公告编号。
     * @return 变更结果。
     */
    @PostMapping("/deleteAttachment")
    ResultBean deleteAttachment(@RequestParam("id") Long id);

    /**
     * 查询已删除并且删除时间超过30天的附件数据。
     *
     * @return 集合
     */
    @PostMapping("/getAttachmentPassList")
    ResultBean<List<AttachmentDTO>> getAttachmentPassList();

    /**
     * 删除已删除并且删除时间超过30天附件数据。
     *
     *
     * @return 变更结果。
     */
    @PostMapping("/deleteAttachmentPass")
    ResultBean deleteAttachmentPass(@RequestParam("id") Long id);
}
