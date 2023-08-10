package com.auiucloud.component.sd.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author dries
 *
 * {
 *   "init_images": [
 *     "string"
 *   ],
 *   "resize_mode": 0,
 *   "denoising_strength": 0.75,
 *   "image_cfg_scale": 0,
 *   "mask": "string",
 *   "mask_blur": 0,
 *   "mask_blur_x": 4,
 *   "mask_blur_y": 4,
 *   "inpainting_fill": 0,
 *   "inpaint_full_res": true,
 *   "inpaint_full_res_padding": 0,
 *   "inpainting_mask_invert": 0,
 *   "initial_noise_multiplier": 0,
 *   "prompt": "",
 *   "styles": [
 *     "string"
 *   ],
 *   "seed": -1,
 *   "subseed": -1,
 *   "subseed_strength": 0,
 *   "seed_resize_from_h": -1,
 *   "seed_resize_from_w": -1,
 *   "sampler_name": "string",
 *   "batch_size": 1,
 *   "n_iter": 1,
 *   "steps": 50,
 *   "cfg_scale": 7,
 *   "width": 512,
 *   "height": 512,
 *   "restore_faces": false,
 *   "tiling": false,
 *   "do_not_save_samples": false,
 *   "do_not_save_grid": false,
 *   "negative_prompt": "string",
 *   "eta": 0,
 *   "s_min_uncond": 0,
 *   "s_churn": 0,
 *   "s_tmax": 0,
 *   "s_tmin": 0,
 *   "s_noise": 1,
 *   "override_settings": {},
 *   "override_settings_restore_afterwards": true,
 *   "script_args": [],
 *   "sampler_index": "Euler",
 *   "include_init_images": false,
 *   "script_name": "string",
 *   "send_images": true,
 *   "save_images": false,
 *   "alwayson_scripts": {}
 * }
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("all")
public class SdImg2ImgParams implements Serializable {

    @Serial
    private static final long serialVersionUID = -1873480972337283008L;

    private List<String> init_images = Collections.emptyList();

    /**
     * 缩放模式
     *
     * 0-Just resize - 仅调整大小
     * 1-Crop and resize - 裁剪后缩放
     * 2-Resize and fill - 缩放后填充空白
     * 3-Just resize (latent upscale) - 仅调整大小 (潜空间放大)
     */
    private int resize_mode = 1;

    /**
     * 去噪强度(高清修复的重绘幅度 默认0.7)
     */
    private float denoising_strength = 0.75F;

    /**
     * 重绘尺寸倍数
     */
    private float image_cfg_scale = 1F;

    /**
     * 遮罩 Mask
     */
    private String mask = "";

    /**
     * 蒙版边缘模糊度，在 0-64 之间调节
     */
    private int mask_blur = 0;

    private int mask_blur_x = 4;

    private int mask_blur_y = 4;

    /**
     * 蒙版区域内容处理
     * 0-fill-填充
     * 1-original-原图
     * 2-latent noise-潜空间噪声
     * 3-latent nothing-空白潜空间
     */
    private int inpainting_fill = 0;

    /**
     *  重绘区域
     *
     *  False - 全图/whole picture
     *  True - 仅蒙版/only masked
     */
    private boolean inpaint_full_res = Boolean.FALSE;

    /**
     * 仅蒙版区域下边缘预留像素 0~256
     */
    private int inpaint_full_res_padding = 0;

    /**
     * 蒙版模式
     * 0-重绘蒙板内容
     * 1-重绘非蒙板内容
     */
    private int inpainting_mask_invert = 0;

    private int initial_noise_multiplier = 0;

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
    private long seed = -1L;

    /**
     * 次级种子
     */
    private long subseed = -1L;

    /**
     * 次级种子强度
     */
    private int subseed_strength = 0;

    /**
     * 次级种子高度
     */
    private int seed_resize_from_h = -1;

    /**
     * 次级种子宽度
     */
    private int seed_resize_from_w = -1;

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
    private int n_iter = 1;

    /**
     * 单批数量
     */
    private int batch_size = 1;

    /**
     * 迭代步数
     */
    private int steps = 30;

    /**
     * 提示词相关性 7
     */
    private float cfg_scale = 7F;

    /**
     * 宽度
     */
    private int width = 512;

    /**
     * 高度
     */
    private int height = 512;

    /**
     * 面部修复
     */
    private boolean restore_faces = Boolean.FALSE;

    /**
     * 平铺图 (Tiling)
     */
    private boolean tiling = Boolean.FALSE;

    /**
     * 不保存样本
     */
    private boolean do_not_save_samples = Boolean.FALSE;

    /**
     * 不保存网格
     */
    private boolean do_not_save_grid = Boolean.FALSE;

    private int eta = 0;
    private int s_min_uncond = 0;
    private int s_churn = 0;
    private int s_tmax = 0;
    private int s_tmin = 0;
    private int s_noise = 1;

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


    private boolean include_init_images= Boolean.FALSE;

    /**
     * 执行脚本名称
     */
    private String script_name;

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
