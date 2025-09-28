package com.example.r024network.Controller;

import com.example.r024network.Exception.APIException;
import com.example.r024network.Result.AjaxResult;
import com.example.r024network.Service.ImageService;
import com.example.r024network.mapper.ImagesMapper;
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
        try{
            imageService.storeFile(file);
        } catch (APIException e) {
            return AjaxResult.fail(e.getStatusCode(), e.getErrorMessage());
        }
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
        try{
            String path = imageService.storeFile(file);
            if (path != null) {
                Integer userAccount = (Integer) request.getAttribute("user_account");
                imageService.updateHeadPortraitDefault(path, userAccount);
            }else {
                return AjaxResult.fail(1, "头像上传失败");
            }
        } catch (APIException e) {
            return AjaxResult.fail(e.getStatusCode(), e.getErrorMessage());
        }
        return AjaxResult.success();
    }
}
