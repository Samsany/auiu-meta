package com.auiucloud.component.cms.domain;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dries
 **/
@Data
public class SdText2ImgConfig implements Serializable {

    @Serial
    private static final long serialVersionUID = 5316008974459562350L;

    /**
     * 启用脸部修复
     */
    private Boolean restore_faces = Boolean.FALSE; // false

    /**
     * 启用高清修复
     */
    private Boolean enable_hr = Boolean.FALSE; // false

    /**
     * 启用平铺分块
     */
    private Boolean tiling = Boolean.FALSE; // false

    /**
     * 高清修复相关:
     * <p>
     * hr_upscaler 放大算法
     * hr_scale 放大倍数
     * hr_second_pass_steps 迭代步数
     * denoising_strength 重绘幅度
     * hr_resize_x 宽度调整
     * hr_resize_y 高度调整
     */
    private Integer hr_scale = 2; // 放大倍数 2
    private String hr_upscaler; // 放大算法
    private Integer hr_second_pass_steps = 20; // 20
    private Integer denoising_strength = 0; // 0
    private Integer hr_resize_x = 0; // 0
    private Integer hr_resize_y = 0; // 0
    private Integer firstphase_width = 0; // 0
    private Integer firstphase_height = 0; // 0

    private String prompt = ""; // 提示词
    private String negative_prompt = ""; // "反向描述"
    private List<String> styles = new ArrayList<>(); // 风格列表 [""]
    private Integer seed = -1; // 种子 -1
    private Integer subseed = -1; // 差异种子 -1
    private Integer subseed_strength = 0; // 差异强度 0
    private Integer seed_resize_from_h = -1; // -1
    private Integer seed_resize_from_w = -1; // -1
    private String sampler_name = "Euler a"; // 采样方法名称 Euler a
    private String sampler_index = "Euler a"; // 采样方法 Euler a,

    private Integer n_iter = 1; // 生成批次 1
    private Integer batch_size = 1; // 每次张数 1

    private Integer steps = 20; // 采样迭代步数 20
    private Integer cfg_scale = 7; // 提示词相关性 7
    private Integer width = 512; // 宽度 512
    private Integer height = 512; // 高度 512

    private Boolean do_not_save_samples = Boolean.FALSE; // false
    private Boolean do_not_save_grid = Boolean.FALSE; // false
    private Integer eta = 0; // 0
    private Integer s_churn = 0; // 0
    private Integer s_tmax = 0; // 0
    private Integer s_tmin = 0; // 0,
    private Integer s_noise = 1; // 1
    /**
     * 一般用于修改本次的生成图片的stable diffusion 模型，用法需保持一致
     *
     *  "override_settings": {
     *      "sd_model_checkpoint" :"wlop-any.ckpt [7331f3bc87]"
     * }
     */
    private Object override_settings;
    private Boolean override_settings_restore_afterwards = Boolean.TRUE; // ":true,

    /**
     * 一般用于lora模型或其他插件参数，如示例，放入了一个lora模型， 1，1为两个权重值，一般只用到前面的权重值1
     * <p>
     *  "script_args": [
     *       0,
     *       true,
     *       true,
     *       "LoRA",
     *       "lora_v1(fa7c1732cc95)",
     *       1,
     *       1
     *   ]
     */
    private List<String> script_args = new ArrayList<>(); // [],
    private String script_name; // null,

    private Boolean send_images = Boolean.TRUE; // true,
    private Boolean save_images = Boolean.FALSE; // false,

    /**
     * 用来存放controlnet相关参数
     * <p>
     *     "alwayson_scripts": {
     *         "ControlNet": {
     *             "args": {
     *                 "enabled": true,
     *                 "module": "none",
     *                 "model": "control_openpose-fp16 [9ca67cc5]",
     *                 "weight": 1,
     *                 "image": "ciascjidhciejoelwmcoejcie"
     *                 "mask": null,
     *                 "invert_image": false,
     *                 "resize_mode": 0,
     *                 "rgbbgr_mode": false,
     *                 "lowvram": false,
     *                 "processor_res": 0,
     *                 "threshold_a": 64,
     *                 "threshold_b": 64,
     *                 "guidance_start": 0,
     *                 "guidance_end": 1,
     *                 "guessmode": false
     *             }
     *         }
     *     }
     */
    private Object alwayson_scripts;

}


// {
//         "enable_hr": false,
//         "denoising_strength": 0,
//         "firstphase_width": 0,
//         "firstphase_height": 0,
//         "hr_scale": 2,
//         "hr_upscaler": "string",
//         "hr_second_pass_steps": 0,
//         "hr_resize_x": 0,
//         "hr_resize_y": 0,
//         "prompt": "",
//         "styles": [
//         "string"
//         ],
//         "seed": -1,
//         "subseed": -1,
//         "subseed_strength": 0,
//         "seed_resize_from_h": -1,
//         "seed_resize_from_w": -1,
//         "sampler_name": "string",
//         "batch_size": 1,
//         "n_iter": 1,
//         "steps": 50,
//         "cfg_scale": 7,
//         "width": 512,
//         "height": 512,
//         "restore_faces": false,
//         "tiling": false,
//         "do_not_save_samples": false,
//         "do_not_save_grid": false,
//         "negative_prompt": "string",
//         "eta": 0,
//         "s_min_uncond": 0,
//         "s_churn": 0,
//         "s_tmax": 0,
//         "s_tmin": 0,
//         "s_noise": 1,
//         "override_settings": {},
//         "override_settings_restore_afterwards": true,
//         "script_args": [],
//         "sampler_index": "Euler",
//         "script_name": "string",
//         "send_images": true,
//         "save_images": false,
//         "alwayson_scripts": {}
//         }
