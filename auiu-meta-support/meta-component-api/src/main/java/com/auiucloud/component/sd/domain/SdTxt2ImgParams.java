package com.auiucloud.component.sd.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author dries
 *
 * // {
 * //         "enable_hr": false,
 * //         "denoising_strength": 0,
 * //         "firstphase_width": 0,
 * //         "firstphase_height": 0,
 * //         "hr_scale": 2,
 * //         "hr_upscaler": "string",
 * //         "hr_second_pass_steps": 0,
 * //         "hr_resize_x": 0,
 * //         "hr_resize_y": 0,
 * //         "prompt": "",
 * //         "styles": [
 * //         "string"
 * //         ],
 * //         "seed": -1,
 * //         "subseed": -1,
 * //         "subseed_strength": 0,
 * //         "seed_resize_from_h": -1,
 * //         "seed_resize_from_w": -1,
 * //         "sampler_name": "string",
 * //         "batch_size": 1,
 * //         "n_iter": 1,
 * //         "steps": 50,
 * //         "cfg_scale": 7,
 * //         "width": 512,
 * //         "height": 512,
 * //         "restore_faces": false,
 * //         "tiling": false,
 * //         "do_not_save_samples": false,
 * //         "do_not_save_grid": false,
 * //         "negative_prompt": "string",
 * //         "eta": 0,
 * //         "s_min_uncond": 0,
 * //         "s_churn": 0,
 * //         "s_tmax": 0,
 * //         "s_tmin": 0,
 * //         "s_noise": 1,
 * //         "override_settings": {},
 * //         "override_settings_restore_afterwards": true,
 * //         "script_args": [],
 * //         "sampler_index": "Euler",
 * //         "script_name": "string",
 * //         "send_images": true,
 * //         "save_images": false,
 * //         "alwayson_scripts": {}
 * //         }
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("all")
public class SdTxt2ImgParams implements Serializable {

    @Serial
    private static final long serialVersionUID = 5316008974459562350L;

    /**
     * 面部修复
     */
    private boolean restore_faces = Boolean.FALSE;

    /**
     * 平铺图 (Tiling)
     */
    private boolean tiling = Boolean.FALSE;

    /**
     * 启用高清修复
     */
    private boolean enable_hr = Boolean.FALSE;
    /**
     * 去噪强度(高清修复的重绘幅度 默认0.7)
     */
    private Float denoising_strength = 0f;

    private Integer firstphase_width = 0;
    private Integer firstphase_height = 0;

    /**
     * 放大算法
     */
    private String hr_upscaler;
    /**
     * 放大倍数
     */
    private Float hr_scale = 2f;
    /**
     * 高清迭代步数
     */
    private Integer hr_second_pass_steps = 0;
    /**
     * 高清修复调整宽度
     */
    private Integer hr_resize_x = 0;
    /**
     * 高清修复调整高度
     */
    private Integer hr_resize_y = 0;
    // private String hr_prompt;
    // private String hr_negative_prompt;

    /**
     * 提示词
     */
    private String prompt;
    /**
     * 反向提示词
     */
    private String negative_prompt;

    /**
     * 提示词模板
     */
    private List<String> styles = Collections.emptyList();

    /**
     * 随机种子 -1
     */
    private Integer seed = -1;

    /**
     * 次级种子
     */
    private Integer subseed = -1;

    /**
     * 次级种子强度
     */
    private Integer subseed_strength = 0;

    /**
     * 次级种子高度
     */
    private Integer seed_resize_from_h = -1;

    /**
     * 次级种子宽度
     */
    private Integer seed_resize_from_w = -1;

    /**
     * 采样器名称 Euler a
     */
    private String sampler_name = "Euler a";

    /**
     * 采样器索引
     */
    private String sampler_index = "Euler a";

    /**
     * 总批次数
     */
    private Integer n_iter = 1;

    /**
     * 单批数量
     */
    private Integer batch_size = 1;

    /**
     * 迭代步数
     */
    private Integer steps = 30;

    /**
     * 提示词相关性 7
     */
    private Float cfg_scale = 7f;

    /**
     * 宽度
     */
    private Integer width = 512;

    /**
     * 高度
     */
    private Integer height = 512;

    /**
     * 不保存样本
     */
    private boolean do_not_save_samples = Boolean.FALSE;

    /**
     * 不保存网格
     */
    private boolean do_not_save_grid = Boolean.FALSE;

    private Integer eta = 0;
    private Integer s_min_uncond = 0;
    private Integer s_churn = 0;
    private Integer s_tmax = 0;
    private Integer s_tmin = 0;
    private Integer s_noise = 1;

    /**
     * 一般用于修改本次的生成图片的stable diffusion 模型，用法需保持一致
     * <p>
     * "override_settings": {
     * "sd_model_checkpoInteger" :"wlop-any.ckpt [7331f3bc87]"
     * }
     */
    private Object override_settings;

    /**
     * 生成之后是否还原设置，默认为TRUE
     */
    private boolean override_settings_restore_afterwards = Boolean.TRUE;

    /**
     * 执行脚本名称
     */
    private String script_name;

    /**
     * 一般用于lora模型或其他插件参数，如示例，放入了一个lora模型， 1，1为两个权重值，一般只用到前面的权重值1
     * <p>
     * "script_args": [
     * 0,
     * true,
     * true,
     * "LoRA",
     * "lora_v1(fa7c1732cc95)",
     * 1,
     * 1
     * ]
     */
    private List<String> script_args = Collections.emptyList();

    /**
     * 发送图片 默认为TRUE
     */
    private boolean send_images = Boolean.TRUE;
    /**
     * 保存图片 默认为FALSE
     */
    private boolean save_images = Boolean.FALSE;

    /**
     * 用来存放controlnet相关参数
     * <p>
     * "alwayson_scripts": {
     * "ControlNet": {
     * "args": {
     * "enabled": true,
     * "module": "none",
     * "model": "control_openpose-fp16 [9ca67cc5]",
     * "weight": 1,
     * "image": "ciascjidhciejoelwmcoejcie"
     * "mask": null,
     * "invert_image": false,
     * "resize_mode": 0,
     * "rgbbgr_mode": false,
     * "lowvram": false,
     * "processor_res": 0,
     * "threshold_a": 64,
     * "threshold_b": 64,
     * "guidance_start": 0,
     * "guidance_end": 1,
     * "guessmode": false
     * }
     * }
     * }
     */
    private Object alwayson_scripts;

}
