##概述
项目包含三个model  
  
app:这是demo  
  
ui:这是UI库  
  
lib:这是负责本地文件读取的I(input)库  
  
##使用方式
强烈推荐使用前.跑一边demo  
  
大致可以分为三种使用方式:  
  
单图选择、多图选择、记录上一次选择的图片模式选择。  
  
效果预览图:  
图有点大。如果不能正常查看，请挂代理或者右键另存为本地查看
![](http://gloomyer.com/img/img/PicSelector.gif)
  
具体调用方式如下:  
  
单图选择:  
```java
UIManager.getInstance().start(this, new OnSelectedListener() {
    @Override
    public void onSelect(List<String> selecteds) {
        Toast.makeText(MainActivity.this, selecteds.toString(), Toast.LENGTH_LONG).show();
    }
});
```
  
多图选择:  
```java
//这里的9代表要选择多少张
UIManager.getInstance().start(this, 9, new OnSelectedListener() {
    @Override
    public void onSelect(List<String> selecteds) {
        Toast.makeText(MainActivity.this, selecteds.toString(), Toast.LENGTH_LONG).show();
    }
});
```
  
带记录的模式多图选择:
```java
//history是一个成员变量，类型是List<String> 可以为null(代表没有记录)
UIManager.getInstance().start(this, 9, false, history, new OnSelectedListener() {
    @Override
    public void onSelect(List<String> selecteds) {
        history = selecteds;
        Toast.makeText(MainActivity.this, selecteds.toString(), Toast.LENGTH_LONG).show();
        UIManager.getInstance().removeOnImageClickListener();
    }
});
```
  
额外的一种模式,可以获取图片点击事件.  
如果设置了这个，那么点击图片将执行用户设置的回调,点击预览的右上角才是选择该图片.  
Demo中的第三个按钮，就是这个模式的演示  
请务必在start之前调用!  
```java
//设置
UIManager.getInstance().setOnImageClickListener(new OnImageClickListener() {
    @Override
    public void onClick(String path) {
        Toast.makeText(MainActivity.this, path, Toast.LENGTH_SHORT).show();
    }
});
//取消设置
UIManager.getInstance().removeOnImageClickListener();
```


欢迎start  
如果发现bug欢迎提交issue!  
博客:[http://gloomyer.com](http://gloomyer.com)  
邮箱:[gloomyneter@gmail.com](mailto:goomyneter@gmail.com)