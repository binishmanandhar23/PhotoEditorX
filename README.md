# PhotoEditorX
Helps you add Stickers, Texts, Images &amp; Emoji's over images and customize them

## Installation

Add the following dependencies in the gradle file of your app module to get started:

```kotlin
implementation 'com.binish.photoeditorx'
```
or if you want to further customize the module, simply import it.

## Setting up the View
First we need to add `PhotoEditorView` in out xml layout

```xml
<com.binish.photoeditorx.photoeditor.PhotoEditorView
        android:id="@+id/photoEditorView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:photo_src="@drawable/nepal_wallpaper"/>
```

We can define our drawable or color resource directly using `app:photo_src`

Or,

We can set the image programmatically by getting source from `PhotoEditorView` which will return a `ImageView` so that we can load image from resources,file or (Picasso/Glide)


```kotlin
val mPhotoEditorView = findViewById(R.id.photoEditorView)
mPhotoEditorView.source.setImageResource(R.drawable.nepal_wallpaper)
```

## Building a PhotoEditor
To use the image editing feature we need to build a PhotoEditor which requires a Context and PhotoEditorView which we have to setup in our xml layout


```kotlin
//Use custom font using latest support library
val mTTCommonBoldTf = ResourcesCompat.getFont(this, R.font.TTCommonBold.otf)

//loading font from assest
val mEmojiTypeFace = Typeface.createFromAsset(getAssets(), "fonts/editFonts/TTCommonBold.otf")

val mPhotoEditor = PhotoEditor.Builder(this, mPhotoEditorView)
         .setPinchTextScalable(true)
         .setDefaultTextTypeface(mTTCommonBoldTf)
         .build()
 ```

We can customize the properties in the PhotoEditor as per our requirement

| Property  | Usage |
| ------------- | ------------- |
| `setPinchTextScalable()`  | set false to disable pinch to zoom on text insertion.By default its true
| `setDefaultTextTypeface()`  | set default text font to be added on image  |
| `setDefaultEmojiTypeface()`  | set default font specifc to add emojis |

That's it we are done with setting up our library


## Text

![](https://imgur.com/IhivwEy)

We can add the text with inputText and colorCode like this

`mPhotoEditor.addText(inputText, colorCode)`

It will take default fonts provided in the builder. If we want different fonts for different text we can set typeface with each text like this

`mPhotoEditor.addText(mTypeface,inputText, colorCode)`

In order to edit the text we need the view, which we will receive in our PhotoEditor callback. This callback will trigger when we **Long Press** the added text

 ```kotlin
 mPhotoEditor.setOnPhotoEditorListener(object: OnPhotoEditorListener() {
            override fun onEditTextChangeListener(rootView: View?, text: String?, colorCode: Int) {
                
            }
        });
  ```
Now we can edit the text with a view like this

`mPhotoEditor.editText(rootView, inputText, colorCode)`

**More Customization**  
For more further customization of texts it is better to build a `TextStyleBuilder()` object & pass it to the `addText()` or `editText()` along with the text
```kotlin
val textStyle = TextStyleBuilder()
        textStyle.withTextFont(mTTCommonBoldTf)
        textStyle.withTextColor(Color.WHITE)
        textStyle.withTextSize(12f)
        textStyle.withTextAlign(TextView.TEXT_ALIGNMENT_TEXT_START)
```
For adding strokes and stroke colors:
```kotlin
    textStyle.withStrokeWidthColor(StrokeProperties(strokeWidth,strokeColor))
```
For adding inner and outer shadows:
```kotlin
    textStyle.withInnerShadow(StrokeProperties(Shadow(r,dx,dy,color), ShadowType.INNER))
    textStyle.withOuterShadow(StrokeProperties(Shadow(r,dx,dy,color), ShadowType.OUTER))
```

## Emoji

![](https://i.imgur.com/RP8kqz6.gif)

We can add the Emoji by `PhotoEditor.getEmojis(getActivity());` which will return a list of emojis unicode.

`mPhotoEditor.addEmoji(emojiUnicode);`

It will take default fonts provided in the builder. If we want different Emoji fonts for different emoji we can set typeface with each Emoji like this

`mPhotoEditor.addEmoji(mEmojiTypeface,emojiUnicode);`

**For more information on Emojis:**
[WIKI](https://github.com/burhanrashid52/PhotoEditor/wiki/Emoji)


## Adding Images/Stickers
 We need to provide a Bitmap to add our Images  `mPhotoEditor.addImage(bitmap);`

 To add dynamic stickers such as Current Time or Date we need to use  
 `photoEditor.addDynamicSticker(TimeView(requireContext())` for time  
 or  
 `photoEditor.addDynamicSticker(DateView(requireContext())` for date.  
 There are two types of design for each of the above view, you can toggle between the types by clicking on the view(Time/Date)  
 or, to manually change them use  
 `timeView.changeView(TimeView.TimerViewType.TYPE_1 or TimeView.TimerViewType.TYPE_2)` for TimeView  
 `dateView.changeView(DateView.DateType.TYPE_1 or DateView.DateType.TYPE_2)` for DateView

 To change fonts for these views use `timeView.changeFont(typeface)` for TimeView & `dateView.changeFont(typeface)` for DateView


## Rotation
In order to rotate the image/bitmap use `photoEditorView.rotate(rotateBy)`
Example:
```kotlin
val rotateBy = PhotoEditorView.Rotation.ROTATE_0 or PhotoEditorView.Rotation.ROTATE_90 or PhotoEditorView.Rotation.ROTATE_180 or PhotoEditorView.Rotation.ROTATE_270
photoEditorView.rotate(rotateBy)
```

## Filter Effect
We can apply inbuild filter to the source images using

 `mPhotoEditor.setFilterEffect(PhotoFilter.BRIGHTNESS);`

![](https://i.imgur.com/xXTGcVC.gif)

We can also apply custom effect using `Custom.Builder`

For more details check [Custom Filters](https://github.com/burhanrashid52/PhotoEditor/wiki/Filter-Effect)


## Undo and Redo

![](https://i.imgur.com/1Y9WcCB.gif)

```kotlin
   mPhotoEditor.undo()
   mPhotoEditor.redo()
```

## Deleting
For deleting, there is a separate view that needs to be added
```xml
<com.binish.photoeditorx.views.DeleteView
        android:id="@+id/viewDelete"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:elevation="10dp"/>
```
and then we can use `viewDelete.onShowDeleteView(view, isInProgress, rawX, rawY)` to bring up the deleteView(it's hidden by default),
to hide it `viewDelete.onHideDeleteView(photoEditor, view, rawX, rawY)`

It is advised to place these on `onMoveViewChangeListener` & `onStopViewChangeListener` to get desired effect.  
************Be sure to check the EXAMPLE on `MainActivity` for better understanding************


## Saving
   We need to provide a file with callback method when edited image is saved

```kotlin
    mPhotoEditor.saveAsFile(filePath, object: PhotoEditor.OnSaveListener {
                    override fun onSuccess(imagePath: String) {
                       Log.e("PhotoEditor","Image Saved Successfully");
                    }

                    @Override
                    override fun onFailure(exception: Exception) {
                        Log.e("PhotoEditor","Failed to save Image");
                    }
                });
```
To get a bitmap as a callback use:
```kotlin
    photoEditor.saveAsBitmap(object : OnSaveBitmap {
                override fun onBitmapReady(saveBitmap: Bitmap?) {
                   
                }

                override fun onFailure(e: Exception?) {
                    
                }
            })
```
