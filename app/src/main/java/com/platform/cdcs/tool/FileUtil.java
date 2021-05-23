package com.platform.cdcs.tool;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.platform.cdcs.MyApp;
import com.trueway.app.uilib.tool.Md5;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.UUID;

import okio.BufferedSink;

/**
 * 类说明
 *
 * @author lisun
 * @date 2013-6-17
 */
public class FileUtil {

    private final static String TAG = FileUtil.class.getSimpleName();
    private final static String[] SIGN_FILE_TYPE = new String[]{"doc", "docx", "xlsx", "xls", "jpg", "png", "jpeg", "bmp", "tif", "ppt", "pptx", "txt", "true", "pdf"};

    /**
     * 创建文件夹
     *
     * @param path
     * @date 2013-6-17 下午1:40:12 (lisun)
     */
    private static void createFolder(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static File getWebViewFile() {
        File file = new File(getBasePath(), "webview");
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    public static File getHtmlFile() {
        File file = new File(getBasePath(), "html");
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    public static File getTurePath() {
        File truePath = new File(getBasePath(), "true");
        if (!truePath.exists()) {
            truePath.mkdir();
        }
        return truePath;
    }

    public static File getWebFile(String filename) {
        File folder = getWebViewFile();
        File file = new File(folder, Md5.encode(filename));
        return file;
    }

    public static void saveWebFile(String content, String filename) {
        //首先查看文件是否存在
        File folder = getWebViewFile();
        File file = new File(folder, Md5.encode(filename));
        if (file.exists()) {

        } else {
            try {
                file.createNewFile();
                FileWriter fw = new FileWriter(file);
                fw.write(content);
                fw.flush();
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static boolean deleteFile(File file) {
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 保存文件
     *
     * @param fileName 文件名
     * @param dirPath  文件路径
     * @return 文件创建是否成功
     * @throws IOException
     */
    public static File createFile(String fileName, String dirPath) {
        createFolder(dirPath);

        File file = new File(dirPath, fileName);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            return file;
        } catch (IOException e) {
            return null;
        }
    }

    public static String copyFile(String source, String folder, String filename) {
        File sources = new File(source);
        if (sources.exists()) {
            if (TextUtils.isEmpty(filename)) {
                filename = UUID.randomUUID().toString();
            }
            File floderFile = new File(getBasePath().getAbsolutePath(), folder);
            if (!floderFile.exists()) {
                floderFile.mkdirs();
            }
            File path = new File(floderFile, filename);
            if (path.exists()) {
                path.delete();
            }
            InputStream fileInputStream = null;
            FileOutputStream fot = null;
            try {
                path.createNewFile();
                fileInputStream = new FileInputStream(sources);
                byte[] buffer = new byte[1024 * 4];
                fot = new FileOutputStream(path);
                int length = 0;
                while ((length = fileInputStream.read(buffer)) != -1) {
                    fot.write(buffer, 0, length);
                }
                return path.getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fot != null) {
                    try {
                        fot.flush();
                        fot.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "";
    }

    public static boolean copyFile(File sourceFile, File copyFile) {
        boolean isSuccess = false;
        if (sourceFile.exists()) {
            InputStream fileInputStream = null;
            FileOutputStream fot = null;
            try {
                if (copyFile.exists()) {
                    copyFile.delete();
                }
                copyFile.createNewFile();
                fileInputStream = new FileInputStream(sourceFile);
                byte[] buffer = new byte[1024 * 4];
                fot = new FileOutputStream(copyFile);
                int length = 0;
                while ((length = fileInputStream.read(buffer)) != -1) {
                    fot.write(buffer, 0, length);
                }
                isSuccess = true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fot != null) {
                    try {
                        fot.flush();
                        fot.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return isSuccess;
            }
        }
        return isSuccess;
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024 * 2;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // try {
            // os.flush();
            // } catch (IOException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
        }
    }

    public static boolean checkExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        } else {
            return false;
        }
    }

    public static File getBasePath() {
        File cacheDir;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            cacheDir = new File(
                    Environment.getExternalStorageDirectory(),
                    "cdcs");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
        } else
            cacheDir = MyApp.getInstance().getApplication().getCacheDir();
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }
        return cacheDir;
    }

    public static File getCloudFile() {
        File file = new File(getBasePath(), "cloud");
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    public static String tempPicPath() {
        String basePath = getBasePath().getAbsolutePath();
        String path = basePath + "/temp";// 得到SD卡得路径
        File mRecAudioPath = new File(path);
        if (!mRecAudioPath.exists()) {
            mRecAudioPath.mkdirs();
        }
        String tempPath = path + File.separator + System.currentTimeMillis()
                + ".jpg";
        return tempPath;
    }

    public static File createRecordFile(String filename) {
        File baseFileFolder = new File(FileUtil.getBasePath(), "record");
        if (!baseFileFolder.exists()) {
            baseFileFolder.mkdirs();
        }
        File file = new File(baseFileFolder, filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                return null;
            }
        }
        return file;
    }

    public static File createNoteFile(String filename) {
        File baseFileFolder = new File(FileUtil.getBasePath(), "note");
        if (!baseFileFolder.exists()) {
            baseFileFolder.mkdirs();
        }
        File file = new File(baseFileFolder, filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                return null;
            }
        }
        return file;
    }

    /**
     * 根据URL得到输入流
     *
     * @param urlStr
     * @return
     */
    public static InputStream getInputStreamFromURL(String urlStr) {
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(urlStr);

            conn.setReadTimeout(30 * 1000);
            conn.setRequestMethod("GET");
            inputStream = conn.getInputStream();
            int fileSize = conn.getContentLength();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return inputStream;
    }

    private static File getFileFromBytes3(String path, String fileName,
                                          InputStream in) {
        File ret = null;
        try {
            ret = new File(path, fileName);
            FileOutputStream nFileOutputStream = new FileOutputStream(ret);
//            long downSize = 0;
            int len = 0;
            byte[] buf = new byte[2 * 1024];
            while ((len = in.read(buf)) != -1) {
//                downSize = downSize + len;
                nFileOutputStream.write(buf, 0, len);
            }
            nFileOutputStream.close();
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return ret;
    }

    /**
     * @param urlStr
     * @param fileName
     * @return -1:文件下载出错 0:文件下载成功 1:文件已经存在
     */
    public static boolean downFile(String urlStr, String dirPath,
                                   String fileName) {
        InputStream inputStream = null;
        try {
            if (createFile(fileName, dirPath) != null) {

                inputStream = getInputStreamFromURL(urlStr);
                if (inputStream != null) {
                    File resultFile = getFileFromBytes3(dirPath, fileName,
                            inputStream);
                    if (resultFile == null) {
                        return false;
                    }
                } else {
                    return false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 临时用
     *
     * @return
     */
    public static String getAvatar(String pid) {
        File file = getBasePath();
        file = new File(file, pid);
        if (!file.exists()) {
            file.mkdir();
        }
        file = new File(file, pid + ".jpg");
        if (!file.exists()) {
            return null;
        }
        return file.getAbsolutePath();
    }

    public static String createMyAvatar(String pid) {
        try {
            File file = getBasePath();
            file = new File(file, pid);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(file, pid + ".jpg");
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getFileType(String path) {
        return path.substring(path.lastIndexOf(".") + 1);
    }

    /**
     *
     * @param 文件名
     * @return 返回相应的资源id
     */
//	public static int getFileDrawable(String filename) {
//		if (TextUtils.isEmpty(filename) || !filename.contains(".")) {
//			return R.drawable.unknow;
//		}
//		int lastIndex = filename.lastIndexOf(".");
//		String typename = filename.substring(lastIndex + 1, filename.length())
//				.toLowerCase();
//		if ("ai".equals(typename)) {
//			return R.drawable.ai;
//		} else if ("bmp".equals(typename) || "jpg".equals(typename)
//				|| "png".equals(typename) || "jpeg".equals(typename)) {
//			return R.drawable.bmp;
//		} else if ("doc".equals(typename) || "docx".equals(typename)) {
//			return R.drawable.doc;
//		} else if ("jpg".equals(typename)) {
//			return R.drawable.jpg;
//		}
//		// else if ("mp3".equals(typename)) {
//		// return R.drawable.mp3;
//		// }
//		else if ("pdf".equals(typename)) {
//			return R.drawable.pdf;
//		}
//		// else if ("png".equals(typename)) {
//		// return R.drawable.png;
//		// }
//		else if ("psd".equals(typename)) {
//			return R.drawable.psd;
//		} else if ("xls".equals(typename) || "xlsx".equals(typename)) {
//			return R.drawable.xls;
//		} else if ("ppt".equals(typename) || "pptx".equals(typename)) {
//			return R.drawable.ppt;
//		} else if ("zip".equals(typename) || "rar".equals(typename)) {
//			return R.drawable.zip;
//		} else if ("txt".equals(typename)) {
//			return R.drawable.text;
//		} else if ("mp3".equals(typename) || "ogg".equals(typename)) {
//			return R.drawable.audio_icon;
//		} else if ("rmvb".equals(typename) || "3gb".equals(typename)
//				|| "mp4".equals(typename) || "avi".equals(typename)) {
//			return R.drawable.video_icon;
//		} else {
//			return R.drawable.unknow;
//		}
//	}

    /**
     * @return 表示文件大小的字符
     */
    public static String getFileLengthString(File file) {
        DecimalFormat df = new DecimalFormat("#.00");
        if (file.length() < 1024) {
            return file.length() + "字节";
        } else if (file.length() < 1024 * 1024) {
            return df.format((file.length() / 1024f)) + "KB";
        } else if (file.length() < 1024 * 1024 * 10) {
            return df.format(file.length() / (1024f * 1024f)) + "MB";
        } else {
            return ">10MB";
        }

    }

    /**
     * @return 表示文件大小的字符
     */
    public static String getFileLengthString(long length) {
        DecimalFormat df = new DecimalFormat("#.00");
        if (length < 1024) {
            return length + "字节";
        } else if (length < 1024 * 1024) {
            return df.format((length / 1024f)) + " KB";
        } else if (length < 1024 * 1024 * 10) {
            return df.format(length / (1024f * 1024f)) + "MB";
        } else {
            return ">10MB";
        }
    }

//    // android获取一个用于打开HTML文件的intent
//    public static Intent getHtmlFileIntent(File file) {
//        Uri uri = Uri.parse(file.toString()).buildUpon()
//                .encodedAuthority("com.android.htmlfileprovider")
//                .scheme("content").encodedPath(file.toString()).build();
//        Intent intent = new Intent("android.intent.action.VIEW");
//        intent.setDataAndType(getUri( MyApp.getInstance().getApplicationContext(),file,intent), "text/html");
//        return intent;
//    }
//
//    // android获取一个用于打开图片文件的intent
//    public static Intent getImageFileIntent(File file) {
//        Intent intent = new Intent("android.intent.action.VIEW");
//        intent.addCategory("android.intent.category.DEFAULT");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setDataAndType(getUri( MyApp.getInstance().getApplicationContext(),file,intent), "image/*");
//        return intent;
//    }
//
//    // android获取一个用于打开PDF文件的intent
//    public static Intent getPdfFileIntent(File file) {
//        Intent intent = new Intent("android.intent.action.VIEW");
//        intent.addCategory("android.intent.category.DEFAULT");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setDataAndType(getUri( MyApp.getInstance().getApplicationContext(),file,intent), "application/pdf");
//        return intent;
//    }
//
//    // android获取一个用于打开文本文件的intent
//    public static Intent getTextFileIntent(File file) {
//        Intent intent = new Intent("android.intent.action.VIEW");
//        intent.addCategory("android.intent.category.DEFAULT");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setDataAndType(getUri( MyApp.getInstance().getApplicationContext(),file,intent), "text/plain");
//        return intent;
//    }
//
//    // android获取一个用于打开音频文件的intent
//    public static Intent getAudioFileIntent(File file) {
//        Intent intent = new Intent("android.intent.action.VIEW");
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("oneshot", 0);
//        intent.putExtra("configchange", 0);
//        intent.setDataAndType(getUri( MyApp.getInstance().getApplicationContext(),file,intent), "audio/*");
//        return intent;
//    }
//
//    // android获取一个用于打开视频文件的intent
//    public static Intent getVideoFileIntent(File file) {
//        Intent intent = new Intent("android.intent.action.VIEW");
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("oneshot", 0);
//        intent.putExtra("configchange", 0);
//        intent.setDataAndType(getUri( MyApp.getInstance().getApplicationContext(),file,intent), "video/*");
//        return intent;
//    }
//
//    // android获取一个用于打开CHM文件的intent
//    public static Intent getChmFileIntent(File file) {
//        Intent intent = new Intent("android.intent.action.VIEW");
//        intent.addCategory("android.intent.category.DEFAULT");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setDataAndType(getUri( MyApp.getInstance().getApplicationContext(),file,intent), "application/x-chm");
//        return intent;
//    }
//
//    // android获取一个用于打开Word文件的intent
//    public static Intent getWordFileIntent(File file) {
//        Intent intent = new Intent("android.intent.action.VIEW");
//        intent.addCategory("android.intent.category.DEFAULT");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setDataAndType(getUri( MyApp.getInstance().getApplicationContext(),file,intent), "application/msword");
//        return intent;
//    }
//
//    // android获取一个用于打开Excel文件的intent
//    public static Intent getExcelFileIntent(File file) {
//        Intent intent = new Intent("android.intent.action.VIEW");
//        intent.addCategory("android.intent.category.DEFAULT");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setDataAndType(getUri( MyApp.getInstance().getApplicationContext(),file,intent), "application/vnd.ms-excel");
//        return intent;
//    }
//
//    // android获取一个用于打开PPT文件的intent
//    public static Intent getPPTFileIntent(File file) {
//        Intent intent = new Intent("android.intent.action.VIEW");
//        intent.addCategory("android.intent.category.DEFAULT");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setDataAndType(getUri( MyApp.getInstance().getApplicationContext(),file,intent),
//                 "application/vnd.ms-powerpoint");
//        return intent;
//    }
//
//    // android获取一个用于打开apk文件的intent
//    public static Intent getApkFileIntent(File file) {
//        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.setDataAndType(getUri( MyApp.getInstance().getApplicationContext(),file,intent),
//                "application/vnd.android.package-archive");
//        return intent;
//    }
//
//    private static boolean checkEndsWithInStringArray(String checkItsEnd,
//                                                      String[] fileEndings) {
//        String lowerCase = checkItsEnd.toLowerCase();
//        for (String aEnd : fileEndings) {
//            if (lowerCase.endsWith(aEnd))
//                return true;
//        }
//        return false;
//    }
//
//    private static Uri getUri(Context context,File file,Intent intent){
//        if (Build.VERSION.SDK_INT >= 24) {
//            Uri imageUri = FileProvider.getUriForFile(context, context.getPackageName()+".fileprovider", file);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
//            return imageUri;
//        } else {
//            return   Uri.fromFile(file);
//        }
//    }
//    private static void toStartActivity(Intent intent, Context context) {
////        PackageManager pm = (PackageManager) context.getPackageManager();
////        List<ResolveInfo> infos = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
////        if (infos.size() > 1) {
//        context.startActivity(intent);
////        } else {
////            Toast.makeText(context, "无法打开，请安装相应的软件！", Toast.LENGTH_SHORT)
////                    .show();
////        }
//    }
//
//    public static void openFile(Context mContext, String fileName,
//                                File currentPath) {
//        if (currentPath.getName().toLowerCase().endsWith(".true")) {
//            //直接打开
////            Intent intent = new Intent(mContext, TrueActivity.class);
////            FileObject obj = new FileObject();
////            obj.setFileTitle(fileName);
////            obj.setMode(FileObject.SIMPLE_PDF);
////            intent.putExtra("itemid", Md5.encode(String.valueOf(System.currentTimeMillis())));
////            intent.putExtra("fileobj", obj);
////            intent.putExtra("type", 1);
////            intent.putExtra("view", true);
////            intent.putExtra("url", currentPath.getAbsolutePath());
////            mContext.startActivity(intent);
//            return;
//        }
//        if (currentPath != null) {
//            Intent intent;
//            try {
//                if (checkEndsWithInStringArray(fileName, mContext
//                        .getResources().getStringArray(R.array.oa_fileEndingImage))) {
//                    intent = getImageFileIntent(currentPath);
//                    toStartActivity(intent, mContext);
//                } else if (checkEndsWithInStringArray(
//                        fileName,
//                        mContext.getResources().getStringArray(
//                                R.array.oa_fileEndingWebText))) {
//                    intent = getHtmlFileIntent(currentPath);
//                    toStartActivity(intent, mContext);
//                } else if (checkEndsWithInStringArray(
//                        fileName,
//                        mContext.getResources().getStringArray(
//                                R.array.oa_fileEndingPackage))) {
//                    intent = getApkFileIntent(currentPath);
//                    toStartActivity(intent, mContext);
//                } else if (checkEndsWithInStringArray(fileName, mContext
//                        .getResources().getStringArray(R.array.oa_fileEndingAudio))) {
//                    intent = getAudioFileIntent(currentPath);
//                    toStartActivity(intent, mContext);
//                } else if (checkEndsWithInStringArray(fileName, mContext
//                        .getResources().getStringArray(R.array.oa_fileEndingVideo))) {
//                    intent = getVideoFileIntent(currentPath);
//                    toStartActivity(intent, mContext);
//                } else if (checkEndsWithInStringArray(fileName, mContext
//                        .getResources().getStringArray(R.array.oa_fileEndingText))) {
//                    intent = getTextFileIntent(currentPath);
//                    toStartActivity(intent, mContext);
//                } else if (checkEndsWithInStringArray(fileName, mContext
//                        .getResources().getStringArray(R.array.oa_fileEndingPdf))) {
//                    intent = getPdfFileIntent(currentPath);
//                    toStartActivity(intent, mContext);
//                } else if (checkEndsWithInStringArray(fileName, mContext
//                        .getResources().getStringArray(R.array.oa_fileEndingWord))) {
//                    intent = getWordFileIntent(currentPath);
//                    toStartActivity(intent, mContext);
//                } else if (checkEndsWithInStringArray(fileName, mContext
//                        .getResources().getStringArray(R.array.oa_fileEndingExcel))) {
//                    intent = getExcelFileIntent(currentPath);
//                    toStartActivity(intent, mContext);
//                } else if (checkEndsWithInStringArray(fileName, mContext
//                        .getResources().getStringArray(R.array.oa_fileEndingPPT))) {
//                    intent = getPPTFileIntent(currentPath);
//                    toStartActivity(intent, mContext);
//                } else {
//                    Toast.makeText(mContext, mContext.getString(R.string.oa_install_soft),
//                            Toast.LENGTH_SHORT).show();
//                }
//            } catch (ActivityNotFoundException e) {
//                e.printStackTrace();
//                Toast.makeText(mContext, mContext.getString(R.string.oa_install_soft), Toast.LENGTH_SHORT)
//                        .show();
//            }
//        } else {
//            Toast.makeText(mContext, mContext.getString(R.string.oa_not_file), Toast.LENGTH_SHORT).show();
//        }
//    }

    public static File getCloudFile(String title) {
        File file = new File(getCloudFile(), Md5.encode(title));
        return file;
    }

    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    public static byte[] readBytesFromFile(String filePath) throws IOException {
        InputStream inStream = new FileInputStream(filePath);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 递归删除缓存
     *
     * @param file
     */
    public static void deleteAllFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteAllFile(files[i]);
                }
            }
            file.delete();
        }
    }

    public static File getPlugin() {
        File file = new File(getBasePath(), "plugin");
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    public static File getMailFolder() {
        File baseFile = getBasePath();
        File mailFile = new File(baseFile, "mail");
        if (!mailFile.exists()) {
            mailFile.mkdir();
        }
        return mailFile;
    }

    public static boolean isSignFile(String filename) {
        if (TextUtils.isEmpty(filename) || !filename.contains(".")) {
            return false;
        }
        String fileType = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
        fileType = fileType.toLowerCase();
        for (String type : SIGN_FILE_TYPE) {
            if (fileType.equals(type)) {
                return true;
            }
        }
        return false;
    }

    public static boolean saveTempTrueFile(String msg) {
        try {
            File trueFile = new File(getBasePath(), "true");
            if (!trueFile.exists()) {
                trueFile.mkdir();
            }
            File trueTempFile = new File(trueFile, "true_temp");
            if (trueTempFile.exists()) {
                trueTempFile.delete();
            }
            trueTempFile.createNewFile();
            FileWriter fw = new FileWriter(trueTempFile);
            fw.write(msg);
            fw.flush();
            fw.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String readTempTrueFile() {
        try {
            File trueFile = new File(getBasePath(), "true");
            File trueTempFile = new File(trueFile, "true_temp");
            if (trueTempFile.exists()) {
                StringBuffer sb = new StringBuffer();
                FileReader fr = new FileReader(trueTempFile);
                BufferedReader reader = new BufferedReader(fr);
                while (reader.ready()) {
                    sb.append(reader.readLine());
                }
                reader.close();
                fr.close();
                return sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void uploadFile(InputStream in, BufferedSink sink)
            throws IOException {
        byte[] buffer = new byte[1024 * 2];
        int read;
        // long total = 0;
        while ((read = in.read(buffer)) != -1) {
            // total += read;
            System.out.println(read + ":============");
            sink.write(buffer, 0, read);
        }
        // return total;
    }

    public static File getImagePath() {
        File videoPath = null;
        File baseFile = getBasePath();
        videoPath = new File(baseFile, "img");
        if (!videoPath.exists()) {
            videoPath.mkdir();
        }
        return videoPath;
    }

    /**
     * 压缩图片方法，为了上传到服务器，保证图片大小在100k一下
     *
     * @param context
     * @param fileSrc
     * @return
     */
    public static File getSmallBitmap(Context context, String fileSrc) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileSrc, options);
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        options.inJustDecodeBounds = false;
        Bitmap img = BitmapFactory.decodeFile(fileSrc, options);
        String filename = context.getFilesDir() + File.separator + "video-" + img.hashCode() + ".jpg";
        saveBitmap2File(img, filename);
        return new File(filename);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int heightRatio = Math.round(height) / reqHeight;
            int widthRatio = Math.round(width) / reqWidth;
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static boolean saveBitmap2File(Bitmap bmp, String filename) {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 50;//压缩50% 100表示不压缩
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bmp.compress(format, quality, stream);
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

// DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        } else {
            String uriStr = uri.toString();
            String path = uriStr.substring(10, uriStr.length());
            if (path.startsWith("com.sec.android.gallery3d")) {
                return null;
            }
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
            }
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            return picturePath;
        }

        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
}
