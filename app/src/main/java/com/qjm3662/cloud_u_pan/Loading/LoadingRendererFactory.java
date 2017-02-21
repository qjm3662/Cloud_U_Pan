package com.qjm3662.cloud_u_pan.Loading;

/**
 * Created by qjm3662 on 2016/11/4 0004.
 */

import android.content.Context;
import android.util.SparseArray;

import java.lang.reflect.Constructor;

/**
 * 加载绘制工厂
 */
public final class LoadingRendererFactory {
    //SparseArray比HashMap更省内存，在某些条件下性能更好，主要是因为它避免了对key的自动装箱（int转为Integer类型），
    // 它内部则是通过两个数组来进行数据存储的，一个存储key，另外一个存储value，为了优化性能，
    // 它内部对数据还采取了压缩的方式来表示稀疏数组的数据，从而节约内存空间，我们从源码中可以看到key和value
    // 分别是用数组表示：
    //该数组可以把所有继承自LoadingRenderer的类的对象加进来
    private static final SparseArray<Class<? extends LoadingRenderer>> LOADING_RENDERERS = new SparseArray<>();

    static {
        //circle rotate
//        LOADING_RENDERERS.put(0, MaterialLoadingRenderer.class);
//        LOADING_RENDERERS.put(1, LevelLoadingRenderer.class);
//        LOADING_RENDERERS.put(2, WhorlLoadingRenderer.class);
//        LOADING_RENDERERS.put(3, GearLoadingRenderer.class);
//        //circle jump
//        LOADING_RENDERERS.put(4, SwapLoadingRenderer.class);
//        LOADING_RENDERERS.put(5, GuardLoadingRenderer.class);
        LOADING_RENDERERS.put(6, DanceLoadingRenderer.class);
//        LOADING_RENDERERS.put(7, CollisionLoadingRenderer.class);
//        //scenery
//        LOADING_RENDERERS.put(8, DayNightLoadingRenderer.class);
//        LOADING_RENDERERS.put(9, ElectricFanLoadingRenderer.class);
//        //animal
//        LOADING_RENDERERS.put(10, FishLoadingRenderer.class);
//        LOADING_RENDERERS.put(11, GhostsEyeLoadingRenderer.class);
//        //goods
//        LOADING_RENDERERS.put(12, BalloonLoadingRenderer.class);
//        LOADING_RENDERERS.put(13, WaterBottleLoadingRenderer.class);
//        //shape change
//        LOADING_RENDERERS.put(14, CircleBroodLoadingRenderer.class);
//        LOADING_RENDERERS.put(15, CoolWaitLoadingRenderer.class);
    }

    //构造函数私有化（实现单例化）
    private LoadingRendererFactory() {
    }

    public static LoadingRenderer createLoadingRenderer(Context context, int loadingRendererId) throws Exception {
        Class<?> loadingRendererClazz = LOADING_RENDERERS.get(loadingRendererId);
        //获取构造函数数组
        Constructor<?>[] constructors = loadingRendererClazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            //获取构造函数参数
            Class<?>[] parameterTypes = constructor.getParameterTypes();

            //选中只有一个参数为Context的构造函数，并把context传入
            if (parameterTypes != null
                    && parameterTypes.length == 1
                    && parameterTypes[0].equals(Context.class)) {
                constructor.setAccessible(true);
                return (LoadingRenderer) constructor.newInstance(context);
            }
        }
        //抛出类型转换失败的异常
        throw new InstantiationException();
    }
}
