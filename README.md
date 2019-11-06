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
// 可以在 Release 页面中查看最新版本并替换版本号
implementation 'com.github.oswayne:AutoGridView:1.0.3'
```


## 快速使用指南

1. 在布局文件中添加 AutoGridView：

```XML
<org.oswayne.view.AutoGridView
    android:id="@+id/giv"
    android:layout_width="240dp"
    android:layout_height="160dp"
    android:background="#F0F0F0"
    app:cellSpacing="12dp"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintLeft_toLeftOf="parent"
    app:layout_constraintRight_toRightOf="parent"
    app:layout_constraintTop_toTopOf="parent"
    app:maxColumn="3"
    app:maxRaw="3"
    app:singleViewHeight="120dp"
    app:singleViewWidth="160dp" />
```

- `maxColumn`: 网格的最大列数
- `maxRaw`: 网格的最大行数
- `cellSpacing`: 网格间距
- `singleViewWidth`: 当网格中只有一个 View 时该 View 的宽度；如不指定则填充 AutoGridView 的宽度
- `singleViewHeight`: 当网格中只有一个 View 时该 View 的高度；如不指定则填充 AutoGridView 的宽度

`singleViewWidth` 和 `singleViewHeight` 必须同时指定，单个指定时不会生效。


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
