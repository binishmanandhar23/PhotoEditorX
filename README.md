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
val mTextRobotoTf = ResourcesCompat.getFont(this, R.font.TTCommonBold.otf)

//loading font from assest
val mEmojiTypeFace = Typeface.createFromAsset(getAssets(), "fonts/editFonts/TTCommonBold.otf")

val mPhotoEditor = PhotoEditor.Builder(this, mPhotoEditorView)
         .setPinchTextScalable(true)
         .setDefaultTextTypeface(mTextRobotoTf)
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

![](https://i.imgur.com/491BmE8.gif)

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


