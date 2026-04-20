package io.github.xxyopen.novel.controller.front;

import io.github.xxyopen.novel.core.common.constant.ErrorCodeEnum;
import io.github.xxyopen.novel.core.common.resp.RestResp;
import io.github.xxyopen.novel.core.constant.ApiRouterConsts;
import io.github.xxyopen.novel.dto.resp.ImgVerifyCodeRespDto;
import io.github.xxyopen.novel.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 前台门户-资源(图片/视频/文档)模块 API 控制器
 */
@Tag(name = "ResourceController", description = "前台门户-资源模块")
@RestController
@RequestMapping(ApiRouterConsts.API_FRONT_RESOURCE_URL_PREFIX)
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @Value("${novel.file.upload.path}")
    private String uploadPath;
    /**
     * 获取图片验证码接口
     */
    @Operation(summary = "获取图片验证码接口")
    @GetMapping("img_verify_code")
    public RestResp<ImgVerifyCodeRespDto> getImgVerifyCode() throws IOException {
        return resourceService.getImgVerifyCode();
    }

    /**
     * 图片上传接口
     */
    @Operation(summary = "图片上传接口")
    @PostMapping("/image")
    public RestResp<String> uploadImage(
        @Parameter(description = "上传文件") @RequestParam("file") MultipartFile file) {
        return resourceService.uploadImage(file);
    }

    @Operation(summary = "文字转语音接口")
    @GetMapping("/tts")
    public RestResp<String> textToSpeech(
            @Parameter(description = "文字内容") @RequestParam String text) throws Exception {

        String ttsDir = uploadPath + "/tts/";
        java.io.File dir = new java.io.File(ttsDir);
        if (!dir.exists()) dir.mkdirs();

        String fileName = System.currentTimeMillis() + ".wav";
        String filePath = (ttsDir + fileName).replace("\\", "/");

        // 清理文本中的单引号避免命令注入
        String safeText = text.replace("'", "").replace("\"", "");

        String psCommand = String.format(
                "Add-Type -AssemblyName System.Speech; " +
                        "$tts = New-Object System.Speech.Synthesis.SpeechSynthesizer; " +
                        "$tts.SetOutputToWaveFile('%s'); " +
                        "$tts.Speak('%s'); " +
                        "$tts.Dispose()",
                filePath, safeText
        );

        ProcessBuilder pb = new ProcessBuilder("powershell", "-Command", psCommand);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        process.waitFor();

        if (!new java.io.File(ttsDir + fileName).exists()) {
            return RestResp.ok("");
        }

        return RestResp.ok("/tts/" + fileName);
    }

}
