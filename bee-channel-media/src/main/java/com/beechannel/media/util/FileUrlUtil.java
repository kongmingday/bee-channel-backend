package com.beechannel.media.util;

import com.beechannel.base.exception.BeeChannelException;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

/**
 * @Description file url process util
 * @Author eotouch
 * @Date 2024/01/02 17:08
 * @Version 1.0
 */
public class FileUrlUtil {

    /**
     * @param fileMd5
     * @param extension
     * @return java.lang.String
     * @description generate folder name by fileMd5
     * @author eotouch
     * @date 2023-12-19 22:33
     */
    public static String generateObjectNameContainFolderBySplit(String fileMd5, String extension) {
        if (!StringUtils.hasText(fileMd5)) {
            BeeChannelException.cast("The file name is incorrect");
        }
        String template = "%s/%s/%s%s";
        return String.format(template, fileMd5.charAt(0) , fileMd5.charAt(1) , fileMd5 , (extension != null ? extension : ""));
    }

    /**
     * @param fileMd5 file md5 code
     * @param index chunck index
     * @return java.lang.String
     * @description generate folder name by fileMd5
     * @author eotouch
     * @date 2023-12-19 22:33
     */
    public static String generateChunkNameContainFolder(String fileMd5, String folderName) {
        if (!StringUtils.hasText(fileMd5)) {
            BeeChannelException.cast("The file name is incorrect");
        }
        String template = "/%s/%s/%s/%s/%s";
        return String.format(template, folderName, fileMd5.charAt(0), fileMd5.charAt(1), fileMd5, "chunk");
    }


    /**
     * @param extension
     * @return java.lang.String
     * @description get the mime type by extension
     * @author eotouch
     * @date 2023-12-18 22:58
     */
    public static String getMimeTypeByExtension(String extension) {

        String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE; // set default mime type
        if (StringUtils.hasText(extension)) {
            ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(extension);
            if (extensionMatch != null) {
                contentType = extensionMatch.getMimeType();
            }
        }
        return contentType;
    }
}
