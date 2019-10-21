# AutoGridView

自动化的网格视图

## 添加依赖

1. 在项目的 `build.gradle` 文件中添加仓库:

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

2. 在模块的 `build.gradle` 文件中引入依赖:

```
implementation 'com.github.oswayne:AutoGridView:1.0.1'
```


## 快速使用指南

1. 在布局文件中添加 AutoGridView：

```XML
<org.carder.view.AutoGridView
    android:id="@+id/giv"
    android:layout_width="240dp"
    android:layout_height="160dp"
    app:cellSpacing="20"
    app:maxColumn="3" />
```

2. 创建适配器：

```Kotlin
class GridImageAdapter(images: List<Int>) : AutoGridAdapter<Int>(images) {

    override fun createView(context: Context, index: Int): View {
        return ImageView(context)
    }

    override fun initView(view: View, index: Int) {
        (view as ImageView).setImageResource(data[index])
    }
}
```

3. 绑定适配器

```Kotlin
// 添加数据源
val imagesId = ArrayList<Int>()
imagesId.add(R.mipmap.ic_launcher)
imagesId.add(R.mipmap.ic_launcher)
imagesId.add(R.mipmap.ic_launcher)
imagesId.add(R.mipmap.ic_launcher)

// 创建并绑定适配器
val adapter = GridImageAdapter(imagesId)
giv.setAdapter(adapter)
```
