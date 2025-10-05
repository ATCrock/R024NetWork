package com.example.r024network.Controller;

import com.example.r024network.Exception.APIException;
import com.example.r024network.Result.AjaxResult;
import com.example.r024network.Service.ImageService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/apifox/image")
@Slf4j
public class ImageController {
    @Resource
    private ImageService imageService;


    /**保存图片（这个接口一般不单独使用，与其他接口混合或包含于其他接口）
     不需要jwt验证
     * @param file 前端传后端的图片文件流
     * @return {@link AjaxResult }
     */
    @PostMapping(value = "/save", consumes ="multipart/form-data")
    public AjaxResult<Object> save(@Valid @RequestBody MultipartFile file){
        if (file == null){
            throw new APIException(410, "文件不能为空");
        }
        imageService.storeFile(file);
        return AjaxResult.success();
    }

    /** 更新头像
     * @param file 前端传后端的图片文件流
     * @param request 前端后端网络请求（需要jwt
     * @return {@link AjaxResult }
     */
    @PutMapping("/updateHead")
    public AjaxResult<Object> updateHead(@Valid @RequestParam MultipartFile file, HttpServletRequest request){
        if (file == null){
            throw new APIException(410, "文件不能为空");
        }
            String fileName = imageService.storeFile(file);
            Integer user_id = (Integer) request.getAttribute("user_id");
            imageService.updateHeadPortraitDefault(fileName, user_id);

        return AjaxResult.success();
    }
}
