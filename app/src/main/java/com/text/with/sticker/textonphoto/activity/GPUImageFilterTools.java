package com.text.with.sticker.textonphoto.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.opengl.Matrix;
import android.util.Log;

import com.text.with.sticker.textonphoto.R;
import com.text.with.sticker.textonphoto.interfacelistner.OnGpuImageFilterChosenListener;

import java.util.LinkedList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage3x3ConvolutionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImage3x3TextureSamplingFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageAddBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageAlphaBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBilateralFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBoxBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBulgeDistortionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageCGAColorspaceFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageChromaKeyBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorBalanceFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorBurnBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorDodgeBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageCrosshatchFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDarkenBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDifferenceBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDilationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDirectionalSobelEdgeDetectionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDissolveBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDivideBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageEmbossFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageExclusionBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageExposureFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFalseColorFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGlassSphereFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHalftoneFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHardLightBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHazeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHighlightShadowFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHueBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageKuwaharaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLaplacianFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLevelsFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLightenBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLinearBurnBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLookupFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLuminosityBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageMonochromeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageMultiplyBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageNonMaximumSuppressionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageNormalBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageOpacityFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageOverlayBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePixelationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePosterizeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageRGBDilationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageRGBFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageScreenBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSketchFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSmoothToonFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSobelEdgeDetection;
import jp.co.cyberagent.android.gpuimage.GPUImageSoftLightBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSourceOverBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSphereRefractionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSubtractBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSwirlFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageToneCurveFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageToonFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageTransformFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageTwoInputFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageVignetteFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageWeakPixelInclusionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageWhiteBalanceFilter;

public class GPUImageFilterTools {

    public enum FilterType {
        CONTRAST,
        GRAYSCALE,
        SHARPEN,
        SEPIA,
        SOBEL_EDGE_DETECTION,
        THREE_X_THREE_CONVOLUTION,
        FILTER_GROUP,
        EMBOSS,
        POSTERIZE,
        GAMMA,
        BRIGHTNESS,
        INVERT,
        HUE,
        PIXELATION,
        SATURATION,
        EXPOSURE,
        HIGHLIGHT_SHADOW,
        MONOCHROME,
        OPACITY,
        RGB,
        WHITE_BALANCE,
        VIGNETTE,
        TONE_CURVE,
        BLEND_COLOR_BURN,
        BLEND_COLOR_DODGE,
        BLEND_DARKEN,
        BLEND_DIFFERENCE,
        BLEND_DISSOLVE,
        BLEND_EXCLUSION,
        BLEND_SOURCE_OVER,
        BLEND_HARD_LIGHT,
        BLEND_LIGHTEN,
        BLEND_ADD,
        BLEND_DIVIDE,
        BLEND_MULTIPLY,
        BLEND_OVERLAY,
        BLEND_SCREEN,
        BLEND_ALPHA,
        BLEND_COLOR,
        BLEND_HUE,
        BLEND_SATURATION,
        BLEND_LUMINOSITY,
        BLEND_LINEAR_BURN,
        BLEND_SOFT_LIGHT,
        BLEND_SUBTRACT,
        BLEND_CHROMA_KEY,
        BLEND_NORMAL,
        LOOKUP_AMATORKA,
        GAUSSIAN_BLUR,
        CROSSHATCH,
        BOX_BLUR,
        CGA_COLORSPACE,
        DILATION,
        KUWAHARA,
        RGB_DILATION,
        SKETCH,
        TOON,
        SMOOTH_TOON,
        BULGE_DISTORTION,
        GLASS_SPHERE,
        HAZE,
        LAPLACIAN,
        NON_MAXIMUM_SUPPRESSION,
        SPHERE_REFRACTION,
        SWIRL,
        WEAK_PIXEL_INCLUSION,
        FALSE_COLOR,
        COLOR_BALANCE,
        LEVELS_FILTER_MIN,
        BILATERAL_BLUR,
        HALFTONE,
        TRANSFORM2D
    }


    static class AnonymousClass2 {
        static final int[] $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType;

        AnonymousClass2() {
        }

        static {
            int[] iArr = new int[FilterType.values().length];
            $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType = iArr;
            try {
                iArr[FilterType.CONTRAST.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.GAMMA.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.INVERT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.PIXELATION.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.HUE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.BRIGHTNESS.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.GRAYSCALE.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.SEPIA.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.SHARPEN.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.SOBEL_EDGE_DETECTION.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.THREE_X_THREE_CONVOLUTION.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.EMBOSS.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.POSTERIZE.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.FILTER_GROUP.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.SATURATION.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.EXPOSURE.ordinal()] = 16;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.HIGHLIGHT_SHADOW.ordinal()] = 17;
            } catch (NoSuchFieldError e17) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.MONOCHROME.ordinal()] = 18;
            } catch (NoSuchFieldError e18) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.OPACITY.ordinal()] = 19;
            } catch (NoSuchFieldError e19) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.RGB.ordinal()] = 20;
            } catch (NoSuchFieldError e20) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.WHITE_BALANCE.ordinal()] = 21;
            } catch (NoSuchFieldError e21) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.VIGNETTE.ordinal()] = 22;
            } catch (NoSuchFieldError e22) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.TONE_CURVE.ordinal()] = 23;
            } catch (NoSuchFieldError e23) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.BLEND_DIFFERENCE.ordinal()] = 24;
            } catch (NoSuchFieldError e24) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.BLEND_SOURCE_OVER.ordinal()] = 25;
            } catch (NoSuchFieldError e25) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.BLEND_COLOR_BURN.ordinal()] = 26;
            } catch (NoSuchFieldError e26) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.BLEND_COLOR_DODGE.ordinal()] = 27;
            } catch (NoSuchFieldError e27) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.BLEND_DARKEN.ordinal()] = 28;
            } catch (NoSuchFieldError e28) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.BLEND_DISSOLVE.ordinal()] = 29;
            } catch (NoSuchFieldError e29) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.BLEND_EXCLUSION.ordinal()] = 30;
            } catch (NoSuchFieldError e30) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.BLEND_HARD_LIGHT.ordinal()] = 31;
            } catch (NoSuchFieldError e31) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.BLEND_LIGHTEN.ordinal()] = 32;
            } catch (NoSuchFieldError e32) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.BLEND_ADD.ordinal()] = 33;
            } catch (NoSuchFieldError e33) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.BLEND_DIVIDE.ordinal()] = 34;
            } catch (NoSuchFieldError e34) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.BLEND_MULTIPLY.ordinal()] = 35;
            } catch (NoSuchFieldError e35) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.BLEND_OVERLAY.ordinal()] = 36;
            } catch (NoSuchFieldError e36) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.BLEND_SCREEN.ordinal()] = 37;
            } catch (NoSuchFieldError e37) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.BLEND_ALPHA.ordinal()] = 38;
            } catch (NoSuchFieldError e38) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.BLEND_COLOR.ordinal()] = 39;
            } catch (NoSuchFieldError e39) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.BLEND_HUE.ordinal()] = 40;
            } catch (NoSuchFieldError e40) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.BLEND_SATURATION.ordinal()] = 41;
            } catch (NoSuchFieldError e41) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.BLEND_LUMINOSITY.ordinal()] = 42;
            } catch (NoSuchFieldError e42) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.BLEND_LINEAR_BURN.ordinal()] = 43;
            } catch (NoSuchFieldError e43) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.BLEND_SOFT_LIGHT.ordinal()] = 44;
            } catch (NoSuchFieldError e44) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.BLEND_SUBTRACT.ordinal()] = 45;
            } catch (NoSuchFieldError e45) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.BLEND_CHROMA_KEY.ordinal()] = 46;
            } catch (NoSuchFieldError e46) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.BLEND_NORMAL.ordinal()] = 47;
            } catch (NoSuchFieldError e47) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.LOOKUP_AMATORKA.ordinal()] = 48;
            } catch (NoSuchFieldError e48) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.GAUSSIAN_BLUR.ordinal()] = 49;
            } catch (NoSuchFieldError e49) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.CROSSHATCH.ordinal()] = 50;
            } catch (NoSuchFieldError e50) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.BOX_BLUR.ordinal()] = 51;
            } catch (NoSuchFieldError e51) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.CGA_COLORSPACE.ordinal()] = 52;
            } catch (NoSuchFieldError e52) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.DILATION.ordinal()] = 53;
            } catch (NoSuchFieldError e53) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.KUWAHARA.ordinal()] = 54;
            } catch (NoSuchFieldError e54) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.RGB_DILATION.ordinal()] = 55;
            } catch (NoSuchFieldError e55) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.SKETCH.ordinal()] = 56;
            } catch (NoSuchFieldError e56) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.TOON.ordinal()] = 57;
            } catch (NoSuchFieldError e57) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.SMOOTH_TOON.ordinal()] = 58;
            } catch (NoSuchFieldError e58) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.BULGE_DISTORTION.ordinal()] = 59;
            } catch (NoSuchFieldError e59) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.GLASS_SPHERE.ordinal()] = 60;
            } catch (NoSuchFieldError e60) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.HAZE.ordinal()] = 61;
            } catch (NoSuchFieldError e61) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.LAPLACIAN.ordinal()] = 62;
            } catch (NoSuchFieldError e62) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.NON_MAXIMUM_SUPPRESSION.ordinal()] = 63;
            } catch (NoSuchFieldError e63) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.SPHERE_REFRACTION.ordinal()] = 64;
            } catch (NoSuchFieldError e64) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.SWIRL.ordinal()] = 65;
            } catch (NoSuchFieldError e65) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.WEAK_PIXEL_INCLUSION.ordinal()] = 66;
            } catch (NoSuchFieldError e66) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.FALSE_COLOR.ordinal()] = 67;
            } catch (NoSuchFieldError e67) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.COLOR_BALANCE.ordinal()] = 68;
            } catch (NoSuchFieldError e68) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.LEVELS_FILTER_MIN.ordinal()] = 69;
            } catch (NoSuchFieldError e69) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.HALFTONE.ordinal()] = 70;
            } catch (NoSuchFieldError e70) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.BILATERAL_BLUR.ordinal()] = 71;
            } catch (NoSuchFieldError e71) {
            }
            try {
                $SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[FilterType.TRANSFORM2D.ordinal()] = 72;
            } catch (NoSuchFieldError e72) {
            }
        }
    }

    public static class FilterAdjuster {
        private final Adjuster<? extends GPUImageFilter> adjuster;

        private abstract class Adjuster<T extends GPUImageFilter> {
            private T filter;

            public abstract void adjust(int i);

            private Adjuster() {
            }

            public Adjuster<T> filter(GPUImageFilter filter2) {
                this.filter = (T) filter2;
                return this;
            }

            public T getFilter() {
                return this.filter;
            }

            /* access modifiers changed from: protected */
            public float range(int percentage, float start, float end) {
                return (((end - start) * ((float) percentage)) / 100.0f) + start;
            }

            /* access modifiers changed from: protected */
            public int range(int percentage, int start, int end) {
                return (((end - start) * percentage) / 100) + start;
            }
        }

        private class BilateralAdjuster extends Adjuster<GPUImageBilateralFilter> {
            private BilateralAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImageBilateralFilter) getFilter()).setDistanceNormalizationFactor(range(percentage, 0.0f, 15.0f));
            }
        }

        private class BrightnessAdjuster extends Adjuster<GPUImageBrightnessFilter> {
            private BrightnessAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImageBrightnessFilter) getFilter()).setBrightness(range(percentage, -1.0f, 1.0f));
            }
        }

        private class BulgeDistortionAdjuster extends Adjuster<GPUImageBulgeDistortionFilter> {
            private BulgeDistortionAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImageBulgeDistortionFilter) getFilter()).setRadius(range(percentage, 0.0f, 1.0f));
                ((GPUImageBulgeDistortionFilter) getFilter()).setScale(range(percentage, -1.0f, 1.0f));
            }
        }

        private class ColorBalanceAdjuster extends Adjuster<GPUImageColorBalanceFilter> {
            private ColorBalanceAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImageColorBalanceFilter) getFilter()).setMidtones(new float[]{range(percentage, 0.0f, 1.0f), range(percentage / 2, 0.0f, 1.0f), range(percentage / 3, 0.0f, 1.0f)});
            }
        }

        private class ContrastAdjuster extends Adjuster<GPUImageContrastFilter> {
            private ContrastAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImageContrastFilter) getFilter()).setContrast(range(percentage, 0.0f, 2.0f));
            }
        }

        private class CrosshatchBlurAdjuster extends Adjuster<GPUImageCrosshatchFilter> {
            private CrosshatchBlurAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImageCrosshatchFilter) getFilter()).setCrossHatchSpacing(range(percentage, 0.0f, 0.06f));
                ((GPUImageCrosshatchFilter) getFilter()).setLineWidth(range(percentage, 0.0f, 0.006f));
            }
        }

        private class DissolveBlendAdjuster extends Adjuster<GPUImageDissolveBlendFilter> {
            private DissolveBlendAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImageDissolveBlendFilter) getFilter()).setMix(range(percentage, 0.0f, 1.0f));
            }
        }

        private class EmbossAdjuster extends Adjuster<GPUImageEmbossFilter> {
            private EmbossAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImageEmbossFilter) getFilter()).setIntensity(range(percentage, 0.0f, 4.0f));
            }
        }

        private class ExposureAdjuster extends Adjuster<GPUImageExposureFilter> {
            private ExposureAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImageExposureFilter) getFilter()).setExposure(range(percentage, -10.0f, 10.0f));
            }
        }

        private class GPU3x3TextureAdjuster extends Adjuster<GPUImage3x3TextureSamplingFilter> {
            private GPU3x3TextureAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImage3x3TextureSamplingFilter) getFilter()).setLineSize(range(percentage, 0.0f, 5.0f));
            }
        }

        private class GammaAdjuster extends Adjuster<GPUImageGammaFilter> {
            private GammaAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImageGammaFilter) getFilter()).setGamma(range(percentage, 0.0f, 3.0f));
            }
        }

        private class GaussianBlurAdjuster extends Adjuster<GPUImageGaussianBlurFilter> {
            private GaussianBlurAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImageGaussianBlurFilter) getFilter()).setBlurSize(range(percentage, 0.0f, 4.5f));
            }
        }

        private class GlassSphereAdjuster extends Adjuster<GPUImageGlassSphereFilter> {
            private GlassSphereAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImageGlassSphereFilter) getFilter()).setRadius(range(percentage, 0.0f, 1.0f));
            }
        }

        private class HazeAdjuster extends Adjuster<GPUImageHazeFilter> {
            private HazeAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImageHazeFilter) getFilter()).setDistance(range(percentage, -0.3f, 0.3f));
                ((GPUImageHazeFilter) getFilter()).setSlope(range(percentage, -0.3f, 0.3f));
            }
        }

        private class HighlightShadowAdjuster extends Adjuster<GPUImageHighlightShadowFilter> {
            private HighlightShadowAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImageHighlightShadowFilter) getFilter()).setShadows(range(percentage, 0.0f, 1.0f));
                ((GPUImageHighlightShadowFilter) getFilter()).setHighlights(range(percentage, 0.0f, 1.0f));
            }
        }

        private class HueAdjuster extends Adjuster<GPUImageHueFilter> {
            private HueAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImageHueFilter) getFilter()).setHue(range(percentage, 0.0f, 360.0f));
            }
        }

        private class LevelsMinMidAdjuster extends Adjuster<GPUImageLevelsFilter> {
            private LevelsMinMidAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImageLevelsFilter) getFilter()).setMin(0.0f, range(percentage, 0.0f, 1.0f), 1.0f);
            }
        }

        private class MonochromeAdjuster extends Adjuster<GPUImageMonochromeFilter> {
            private MonochromeAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImageMonochromeFilter) getFilter()).setIntensity(range(percentage, 0.0f, 1.0f));
            }
        }

        private class OpacityAdjuster extends Adjuster<GPUImageOpacityFilter> {
            private OpacityAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImageOpacityFilter) getFilter()).setOpacity(range(percentage, 0.0f, 1.0f));
            }
        }

        private class PixelationAdjuster extends Adjuster<GPUImagePixelationFilter> {
            private PixelationAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImagePixelationFilter) getFilter()).setPixel(range(percentage, 1.0f, 100.0f));
            }
        }

        private class PosterizeAdjuster extends Adjuster<GPUImagePosterizeFilter> {
            private PosterizeAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImagePosterizeFilter) getFilter()).setColorLevels(range(percentage, 1, 50));
            }
        }

        private class RGBAdjuster extends Adjuster<GPUImageRGBFilter> {
            private RGBAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImageRGBFilter) getFilter()).setRed(range(percentage, 0.0f, 1.0f));
            }
        }

        private class RotateAdjuster extends Adjuster<GPUImageTransformFilter> {
            private RotateAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                float[] transform = new float[16];
                Matrix.setRotateM(transform, 0, (float) ((percentage * 360) / 100), 0.0f, 0.0f, 1.0f);
                ((GPUImageTransformFilter) getFilter()).setTransform3D(transform);
            }
        }

        private class SaturationAdjuster extends Adjuster<GPUImageSaturationFilter> {
            private SaturationAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImageSaturationFilter) getFilter()).setSaturation(range(percentage, 0.0f, 2.0f));
            }
        }

        private class SepiaAdjuster extends Adjuster<GPUImageSepiaFilter> {
            private SepiaAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImageSepiaFilter) getFilter()).setIntensity(range(percentage, 0.0f, 2.0f));
            }
        }

        private class SharpnessAdjuster extends Adjuster<GPUImageSharpenFilter> {
            private SharpnessAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImageSharpenFilter) getFilter()).setSharpness(range(percentage, -4.0f, 4.0f));
            }
        }

        private class SobelAdjuster extends Adjuster<GPUImageSobelEdgeDetection> {
            private SobelAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImageSobelEdgeDetection) getFilter()).setLineSize(range(percentage, 0.0f, 5.0f));
            }
        }

        private class SphereRefractionAdjuster extends Adjuster<GPUImageSphereRefractionFilter> {
            private SphereRefractionAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImageSphereRefractionFilter) getFilter()).setRadius(range(percentage, 0.0f, 1.0f));
            }
        }

        private class SwirlAdjuster extends Adjuster<GPUImageSwirlFilter> {
            private SwirlAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImageSwirlFilter) getFilter()).setAngle(range(percentage, 0.0f, 2.0f));
            }
        }

        private class VignetteAdjuster extends Adjuster<GPUImageVignetteFilter> {
            private VignetteAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImageVignetteFilter) getFilter()).setVignetteStart(range(percentage, 0.0f, 1.0f));
            }
        }

        private class WhiteBalanceAdjuster extends Adjuster<GPUImageWhiteBalanceFilter> {
            private WhiteBalanceAdjuster() {
                super();
            }

            public void adjust(int percentage) {
                ((GPUImageWhiteBalanceFilter) getFilter()).setTemperature(range(percentage, 2000.0f, 8000.0f));
            }
        }

        public FilterAdjuster(GPUImageFilter filter) {
            if (filter instanceof GPUImageSharpenFilter) {
                this.adjuster = new SharpnessAdjuster().filter(filter);
            } else if (filter instanceof GPUImageSepiaFilter) {
                this.adjuster = new SepiaAdjuster().filter(filter);
            } else if (filter instanceof GPUImageContrastFilter) {
                this.adjuster = new ContrastAdjuster().filter(filter);
            } else if (filter instanceof GPUImageGammaFilter) {
                this.adjuster = new GammaAdjuster().filter(filter);
            } else if (filter instanceof GPUImageBrightnessFilter) {
                this.adjuster = new BrightnessAdjuster().filter(filter);
            } else if (filter instanceof GPUImageSobelEdgeDetection) {
                this.adjuster = new SobelAdjuster().filter(filter);
            } else if (filter instanceof GPUImageEmbossFilter) {
                this.adjuster = new EmbossAdjuster().filter(filter);
            } else if (filter instanceof GPUImage3x3TextureSamplingFilter) {
                this.adjuster = new GPU3x3TextureAdjuster().filter(filter);
            } else if (filter instanceof GPUImageHueFilter) {
                this.adjuster = new HueAdjuster().filter(filter);
            } else if (filter instanceof GPUImagePosterizeFilter) {
                this.adjuster = new PosterizeAdjuster().filter(filter);
            } else if (filter instanceof GPUImagePixelationFilter) {
                this.adjuster = new PixelationAdjuster().filter(filter);
            } else if (filter instanceof GPUImageSaturationFilter) {
                this.adjuster = new SaturationAdjuster().filter(filter);
            } else if (filter instanceof GPUImageExposureFilter) {
                this.adjuster = new ExposureAdjuster().filter(filter);
            } else if (filter instanceof GPUImageHighlightShadowFilter) {
                this.adjuster = new HighlightShadowAdjuster().filter(filter);
            } else if (filter instanceof GPUImageMonochromeFilter) {
                this.adjuster = new MonochromeAdjuster().filter(filter);
            } else if (filter instanceof GPUImageOpacityFilter) {
                this.adjuster = new OpacityAdjuster().filter(filter);
            } else if (filter instanceof GPUImageRGBFilter) {
                this.adjuster = new RGBAdjuster().filter(filter);
            } else if (filter instanceof GPUImageWhiteBalanceFilter) {
                this.adjuster = new WhiteBalanceAdjuster().filter(filter);
            } else if (filter instanceof GPUImageVignetteFilter) {
                this.adjuster = new VignetteAdjuster().filter(filter);
            } else if (filter instanceof GPUImageDissolveBlendFilter) {
                this.adjuster = new DissolveBlendAdjuster().filter(filter);
            } else if (filter instanceof GPUImageGaussianBlurFilter) {
                this.adjuster = new GaussianBlurAdjuster().filter(filter);
            } else if (filter instanceof GPUImageCrosshatchFilter) {
                this.adjuster = new CrosshatchBlurAdjuster().filter(filter);
            } else if (filter instanceof GPUImageBulgeDistortionFilter) {
                this.adjuster = new BulgeDistortionAdjuster().filter(filter);
            } else if (filter instanceof GPUImageGlassSphereFilter) {
                this.adjuster = new GlassSphereAdjuster().filter(filter);
            } else if (filter instanceof GPUImageHazeFilter) {
                this.adjuster = new HazeAdjuster().filter(filter);
            } else if (filter instanceof GPUImageSphereRefractionFilter) {
                this.adjuster = new SphereRefractionAdjuster().filter(filter);
            } else if (filter instanceof GPUImageSwirlFilter) {
                this.adjuster = new SwirlAdjuster().filter(filter);
            } else if (filter instanceof GPUImageColorBalanceFilter) {
                this.adjuster = new ColorBalanceAdjuster().filter(filter);
            } else if (filter instanceof GPUImageLevelsFilter) {
                this.adjuster = new LevelsMinMidAdjuster().filter(filter);
            } else if (filter instanceof GPUImageBilateralFilter) {
                this.adjuster = new BilateralAdjuster().filter(filter);
            } else if (filter instanceof GPUImageTransformFilter) {
                this.adjuster = new RotateAdjuster().filter(filter);
            } else {
                this.adjuster = null;
            }
        }

        public boolean canAdjust() {
            return this.adjuster != null;
        }

        public void adjust(int percentage) {
            Adjuster<? extends GPUImageFilter> adjuster2 = this.adjuster;
            if (adjuster2 != null) {
                adjuster2.adjust(percentage);
            }
        }
    }

    private static class FilterList {
        public List<FilterType> filters;
        public List<String> names;

        private FilterList() {
            this.names = new LinkedList();
            this.filters = new LinkedList();
        }

        public void addFilter(String name, FilterType filter) {
            this.names.add(name);
            this.filters.add(filter);
        }
    }

    public static void showDialog(final Context context, final OnGpuImageFilterChosenListener listener) {
        final FilterList filters = new FilterList();
        filters.addFilter("Contrast", FilterType.CONTRAST);
        filters.addFilter("Invert", FilterType.INVERT);
        filters.addFilter("Pixelation", FilterType.PIXELATION);
        filters.addFilter("Hue", FilterType.HUE);
        filters.addFilter("Gamma", FilterType.GAMMA);
        filters.addFilter("Brightness", FilterType.BRIGHTNESS);
        filters.addFilter("Sepia", FilterType.SEPIA);
        filters.addFilter("Grayscale", FilterType.GRAYSCALE);
        filters.addFilter("Sharpness", FilterType.SHARPEN);
        filters.addFilter("Sobel Edge Detection", FilterType.SOBEL_EDGE_DETECTION);
        filters.addFilter("3x3 Convolution", FilterType.THREE_X_THREE_CONVOLUTION);
        filters.addFilter("Emboss", FilterType.EMBOSS);
        filters.addFilter("Posterize", FilterType.POSTERIZE);
        filters.addFilter("Grouped filters", FilterType.FILTER_GROUP);
        filters.addFilter("Saturation", FilterType.SATURATION);
        filters.addFilter("Exposure", FilterType.EXPOSURE);
        filters.addFilter("Highlight Shadow", FilterType.HIGHLIGHT_SHADOW);
        filters.addFilter("Monochrome", FilterType.MONOCHROME);
        filters.addFilter("Opacity", FilterType.OPACITY);
        filters.addFilter("RGB", FilterType.RGB);
        filters.addFilter("White Balance", FilterType.WHITE_BALANCE);
        filters.addFilter("Vignette", FilterType.VIGNETTE);
        filters.addFilter("ToneCurve", FilterType.TONE_CURVE);
        filters.addFilter("Blend (Difference)", FilterType.BLEND_DIFFERENCE);
        filters.addFilter("Blend (Source Over)", FilterType.BLEND_SOURCE_OVER);
        filters.addFilter("Blend (Color Burn)", FilterType.BLEND_COLOR_BURN);
        filters.addFilter("Blend (Color Dodge)", FilterType.BLEND_COLOR_DODGE);
        filters.addFilter("Blend (Darken)", FilterType.BLEND_DARKEN);
        filters.addFilter("Blend (Dissolve)", FilterType.BLEND_DISSOLVE);
        filters.addFilter("Blend (Exclusion)", FilterType.BLEND_EXCLUSION);
        filters.addFilter("Blend (Hard Light)", FilterType.BLEND_HARD_LIGHT);
        filters.addFilter("Blend (Lighten)", FilterType.BLEND_LIGHTEN);
        filters.addFilter("Blend (Add)", FilterType.BLEND_ADD);
        filters.addFilter("Blend (Divide)", FilterType.BLEND_DIVIDE);
        filters.addFilter("Blend (Multiply)", FilterType.BLEND_MULTIPLY);
        filters.addFilter("Blend (Overlay)", FilterType.BLEND_OVERLAY);
        filters.addFilter("Blend (Screen)", FilterType.BLEND_SCREEN);
        filters.addFilter("Blend (Alpha)", FilterType.BLEND_ALPHA);
        filters.addFilter("Blend (Color)", FilterType.BLEND_COLOR);
        filters.addFilter("Blend (Hue)", FilterType.BLEND_HUE);
        filters.addFilter("Blend (Saturation)", FilterType.BLEND_SATURATION);
        filters.addFilter("Blend (Luminosity)", FilterType.BLEND_LUMINOSITY);
        filters.addFilter("Blend (Linear Burn)", FilterType.BLEND_LINEAR_BURN);
        filters.addFilter("Blend (Soft Light)", FilterType.BLEND_SOFT_LIGHT);
        filters.addFilter("Blend (Subtract)", FilterType.BLEND_SUBTRACT);
        filters.addFilter("Blend (Chroma Key)", FilterType.BLEND_CHROMA_KEY);
        filters.addFilter("Blend (Normal)", FilterType.BLEND_NORMAL);
        filters.addFilter("Lookup (Amatorka)", FilterType.LOOKUP_AMATORKA);
        filters.addFilter("Gaussian Blur", FilterType.GAUSSIAN_BLUR);
        filters.addFilter("Crosshatch", FilterType.CROSSHATCH);
        filters.addFilter("Box Blur", FilterType.BOX_BLUR);
        filters.addFilter("CGA Color Space", FilterType.CGA_COLORSPACE);
        filters.addFilter("Dilation", FilterType.DILATION);
        filters.addFilter("Kuwahara", FilterType.KUWAHARA);
        filters.addFilter("RGB Dilation", FilterType.RGB_DILATION);
        filters.addFilter("Sketch", FilterType.SKETCH);
        filters.addFilter("Toon", FilterType.TOON);
        filters.addFilter("Smooth Toon", FilterType.SMOOTH_TOON);
        filters.addFilter("Halftone", FilterType.HALFTONE);
        filters.addFilter("Bulge Distortion", FilterType.BULGE_DISTORTION);
        filters.addFilter("Glass Sphere", FilterType.GLASS_SPHERE);
        filters.addFilter("Haze", FilterType.HAZE);
        filters.addFilter("Laplacian", FilterType.LAPLACIAN);
        filters.addFilter("Non Maximum Suppression", FilterType.NON_MAXIMUM_SUPPRESSION);
        filters.addFilter("Sphere Refraction", FilterType.SPHERE_REFRACTION);
        filters.addFilter("Swirl", FilterType.SWIRL);
        filters.addFilter("Weak Pixel Inclusion", FilterType.WEAK_PIXEL_INCLUSION);
        filters.addFilter("False Color", FilterType.FALSE_COLOR);
        filters.addFilter("Color Balance", FilterType.COLOR_BALANCE);
        filters.addFilter("Levels Min (Mid Adjust)", FilterType.LEVELS_FILTER_MIN);
        filters.addFilter("Bilateral Blur", FilterType.BILATERAL_BLUR);
        filters.addFilter("Transform (2-D)", FilterType.TRANSFORM2D);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose a filter");
        builder.setItems((CharSequence[]) filters.names.toArray(new String[filters.names.size()]), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                OnGpuImageFilterChosenListener.onGpuImageFilterChosenListener(GPUImageFilterTools.createFilterForType(context, filters.filters.get(item)));
            }
        });
        builder.create().show();
    }

    static GPUImageFilter createFilterForType(Context context, FilterType type) {
        Log.i("filtertype", "" + type);
        switch (AnonymousClass2.$SwitchMap$com$SimplyEntertaining$coolquotes$activity$GPUImageFilterTools$FilterType[type.ordinal()]) {
            case 1:
                return new GPUImageContrastFilter(2.0f);
            case 2:
                return new GPUImageGammaFilter(2.0f);
            case 3:
                return new GPUImageColorInvertFilter();
            case 4:
                return new GPUImagePixelationFilter();
            case 5:
                return new GPUImageHueFilter(90.0f);
            case 6:
                return new GPUImageBrightnessFilter(1.5f);
            case 7:
                return new GPUImageGrayscaleFilter();
            case 8:
                return new GPUImageSepiaFilter();
            case 9:
                GPUImageSharpenFilter sharpness = new GPUImageSharpenFilter();
                sharpness.setSharpness(2.0f);
                return sharpness;
            case 10:
                return new GPUImageSobelEdgeDetection();
            case 11:
                GPUImage3x3ConvolutionFilter convolution = new GPUImage3x3ConvolutionFilter();
                convolution.setConvolutionKernel(new float[]{-1.0f, 0.0f, 1.0f, -2.0f, 0.0f, 2.0f, -1.0f, 0.0f, 1.0f});
                return convolution;
            case 12:
                return new GPUImageEmbossFilter();
            case 13:
                return new GPUImagePosterizeFilter();
            case 14:
                List<GPUImageFilter> filters = new LinkedList<>();
                filters.add(new GPUImageContrastFilter());
                filters.add(new GPUImageDirectionalSobelEdgeDetectionFilter());
                filters.add(new GPUImageGrayscaleFilter());
                return new GPUImageFilterGroup(filters);
            case 15:
                return new GPUImageSaturationFilter(1.0f);
            case 16:
                return new GPUImageExposureFilter(0.0f);
            case 17:
                return new GPUImageHighlightShadowFilter(0.0f, 1.0f);
            case 18:
                return new GPUImageMonochromeFilter(1.0f, new float[]{0.6f, 0.45f, 0.3f, 1.0f});
            case 19:
                return new GPUImageOpacityFilter(1.0f);
            case 20:
                return new GPUImageRGBFilter(1.0f, 1.0f, 1.0f);
            case 21:
                return new GPUImageWhiteBalanceFilter(5000.0f, 0.0f);
            case 22:
                PointF centerPoint = new PointF();
                centerPoint.x = 0.5f;
                centerPoint.y = 0.5f;
                return new GPUImageVignetteFilter(centerPoint, new float[]{0.0f, 0.0f, 0.0f}, 0.3f, 0.75f);
            case 23:
                GPUImageToneCurveFilter toneCurveFilter = new GPUImageToneCurveFilter();
                toneCurveFilter.setFromCurveFileInputStream(context.getResources().openRawResource(R.raw.tone_cuver_sample));
                return toneCurveFilter;
            case 24:
                return createBlendFilter(context, GPUImageDifferenceBlendFilter.class);
            case 25:
                return createBlendFilter(context, GPUImageSourceOverBlendFilter.class);
            case 26:
                return createBlendFilter(context, GPUImageColorBurnBlendFilter.class);
            case 27:
                return createBlendFilter(context, GPUImageColorDodgeBlendFilter.class);
            case 28:
                return createBlendFilter(context, GPUImageDarkenBlendFilter.class);
            case 29:
                return createBlendFilter(context, GPUImageDissolveBlendFilter.class);
            case 30:
                return createBlendFilter(context, GPUImageExclusionBlendFilter.class);
            case 31:
                return createBlendFilter(context, GPUImageHardLightBlendFilter.class);
            case 32:
                return createBlendFilter(context, GPUImageLightenBlendFilter.class);
            case 33:
                return createBlendFilter(context, GPUImageAddBlendFilter.class);
            case 34:
                return createBlendFilter(context, GPUImageDivideBlendFilter.class);
            case 35:
                return createBlendFilter(context, GPUImageMultiplyBlendFilter.class);
            case 36:
                return createBlendFilter(context, GPUImageOverlayBlendFilter.class);
            case 37:
                return createBlendFilter(context, GPUImageScreenBlendFilter.class);
            case 38:
                return createBlendFilter(context, GPUImageAlphaBlendFilter.class);
            case 39:
                return createBlendFilter(context, GPUImageColorBlendFilter.class);
            case 40:
                return createBlendFilter(context, GPUImageHueBlendFilter.class);
            case 41:
                return createBlendFilter(context, GPUImageSaturationBlendFilter.class);
            case 42:
                return createBlendFilter(context, GPUImageLuminosityBlendFilter.class);
            case 43:
                return createBlendFilter(context, GPUImageLinearBurnBlendFilter.class);
            case 44:
                return createBlendFilter(context, GPUImageSoftLightBlendFilter.class);
            case 45:
                return createBlendFilter(context, GPUImageSubtractBlendFilter.class);
            case 46:
                return createBlendFilter(context, GPUImageChromaKeyBlendFilter.class);
            case 47:
                return createBlendFilter(context, GPUImageNormalBlendFilter.class);
            case 48:
                GPUImageLookupFilter amatorka = new GPUImageLookupFilter();
                amatorka.setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.lookup_amatorka));
                return amatorka;
            case 49:
                return new GPUImageGaussianBlurFilter();
            case 50:
                return new GPUImageCrosshatchFilter();
            case 51:
                return new GPUImageBoxBlurFilter();
            case 52:
                return new GPUImageCGAColorspaceFilter();
            case 53:
                return new GPUImageDilationFilter();
            case 54:
                return new GPUImageKuwaharaFilter();
            case 55:
                return new GPUImageRGBDilationFilter();
            case 56:
                return new GPUImageSketchFilter();
            case 57:
                return new GPUImageToonFilter();
            case 58:
                return new GPUImageSmoothToonFilter();
            case 59:
                return new GPUImageBulgeDistortionFilter();
            case 60:
                return new GPUImageGlassSphereFilter();
            case 61:
                return new GPUImageHazeFilter();
            case 62:
                return new GPUImageLaplacianFilter();
            case 63:
                return new GPUImageNonMaximumSuppressionFilter();
            case 64:
                return new GPUImageSphereRefractionFilter();
            case 65:
                return new GPUImageSwirlFilter();
            case 66:
                return new GPUImageWeakPixelInclusionFilter();
            case 67:
                return new GPUImageFalseColorFilter();
            case 68:
                return new GPUImageColorBalanceFilter();
            case 69:
                GPUImageLevelsFilter levelsFilter = new GPUImageLevelsFilter();
                levelsFilter.setMin(0.0f, 3.0f, 1.0f);
                return levelsFilter;
            case 70:
                return new GPUImageHalftoneFilter();
            case 71:
                return new GPUImageBilateralFilter();
            case 72:
                return new GPUImageTransformFilter();
            default:
                throw new IllegalStateException("No filter of that type!");
        }
    }

    private static GPUImageFilter createBlendFilter(Context context, Class<? extends GPUImageTwoInputFilter> filterClass) {
        try {
            GPUImageTwoInputFilter filter = (GPUImageTwoInputFilter) filterClass.newInstance();
            filter.setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.blue_bg));
            return filter;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
