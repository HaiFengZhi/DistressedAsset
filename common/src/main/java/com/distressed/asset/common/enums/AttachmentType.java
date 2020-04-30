package com.distressed.asset.common.enums;

/**
 * 附件类型枚举。
 *
 * @author SuperZhao
 * @version 1.0
 * @date 2019-11-19 22:10
 */
public enum AttachmentType {
    JPG, PNG, GIF, BMP, PDF, DOC, DOCX, XLS, XLSX, TXT, VIDEO, UNKNOWN;

    /**
     * 通过类型名称创建{@link com.online.shop.common.enums.AttachmentType}类型。
     *
     * @param type 类型名称，必须是JPG, PNG, GIF, BMP, PDF, DOC, DOCX, XLS, XLSX, TXT, UNKNOWN其中之一。
     * @return {@link com.online.shop.common.enums.AttachmentType}类型。
     */
    public static AttachmentType create(String type) {
        type = (type == null) ? "UNKNOWN" : type.trim().toUpperCase();
        int index;
        try {
            return AttachmentType.valueOf(type.substring((index = type.lastIndexOf('.')) > -1 ? index + 1 : 0));
        } catch (IllegalArgumentException ex) {
            return AttachmentType.UNKNOWN;
        }
    }
}
