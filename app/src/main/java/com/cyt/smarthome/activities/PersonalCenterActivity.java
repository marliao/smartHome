package com.cyt.smarthome.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Picture;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyt.smarthome.R;
import com.cyt.smarthome.bean.User;
import com.cyt.smarthome.utils.ImageUtils;
import com.cyt.smarthome.utils.L;
import com.cyt.smarthome.utils.PackageCode;
import com.cyt.smarthome.utils.T;
import com.cyt.smarthome.view.CustomTitleBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;

public class PersonalCenterActivity extends BaseActivity {
    private CustomTitleBar tvPersonalCenter;
    private ImageView ivUIcon;
    private TextView tvUName;
    private TextView tvCarNumber;
    private TextView tvCellphoneNumber;
    private TextView tvAdress;
    private TextView tv_u_per;
    private AlertDialog alertDialog;

    @Override
    protected int getLayout() {
        return R.layout.activity_personalcenter;
    }

    @Override
    protected void initLayout() {
        tvPersonalCenter = (CustomTitleBar) findViewById(R.id.tv_personal_center);
        ivUIcon = (ImageView) findViewById(R.id.iv_u_icon);
        tvUName = (TextView) findViewById(R.id.tv_u_name);
        tvCarNumber = (TextView) findViewById(R.id.tv_CarNumber);
        tvCellphoneNumber = (TextView) findViewById(R.id.tv_CellphoneNumber);
        tvAdress = (TextView) findViewById(R.id.tv_adress);
        tv_u_per = (TextView) findViewById(R.id.tv_u_per);
    }

    @Override
    protected void init() {
        User user = BmobUser.getCurrentUser(User.class);
        if (user.getuName() != null) {
            tvUName.setText(user.getuName());
        }
//        L.e("user.getLevel()"+user.getLevel());
        if (user.getOwner_name() != null) {
            tv_u_per.setText("【住户】");
        } else {
            tv_u_per.setText("【业主】");
        }
        tvCarNumber.setText(user.getIdentity_num().substring(0, 10) + "****" + user.getIdentity_num().substring(14, 18));
        tvCellphoneNumber.setText(user.getUsername());
        tvAdress.setText(user.getFloor_id() + "号楼" + user.getUnit_num() + "单元" + user.getFloor() + "层" + user.getDoor_num() + "室");
        tvPersonalCenter.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });
        if(user.getAvatar()!=null){
            L.e(user.getAvatar().getUrl());
            ImageUtils.setCircleBitmap(user.getAvatar().getUrl(),ivUIcon );
        }
        ivUIcon.setOnClickListener(v -> showAlertDialog());

    }
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonalCenterActivity.this);
        alertDialog = builder.create();
        View inflate = View.inflate(PersonalCenterActivity.this, R.layout.main_tag3_dialog, null);

        alertDialog.setView(inflate);
        alertDialog.show();
        TextView mCardView1;
        TextView mCardView2;
        TextView mCardView3;

        mCardView1 = (TextView) inflate.findViewById(R.id.cardView1);
        mCardView2 = (TextView) inflate.findViewById(R.id.cardView2);
        mCardView3 = (TextView) inflate.findViewById(R.id.cardView3);
        mCardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        mCardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(PersonalCenterActivity.this)
                        .openCamera(PictureMimeType.ofImage())
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                alertDialog.dismiss();
            }
        });
        mCardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(PersonalCenterActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                alertDialog.dismiss();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    L.e("----zu----------" + selectList.size());

                    final String[] filePaths = new String[selectList.size()];
                    for (int i = 0; i < selectList.size(); i++) {
                        filePaths[i] = selectList.get(i).getPath();
                    }

                    ProgressDialog progressDialog = new ProgressDialog(PersonalCenterActivity.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setTitle("图片上传");
                    progressDialog.setMax(100);
//                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setIndeterminate(false);
                    progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            T.showShort("取消上传");
                        }
                    });
                    BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
                        @Override
                        public void onSuccess(List<BmobFile> files, List<String> urls) {
                            //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                            //2、urls-上传文件的完整url地址
                            if (urls.size() == filePaths.length) {//如果数量相等，则代表文件全部上传完成
                                //do something
                                progressDialog.dismiss();
                                User currentUser = BmobUser.getCurrentUser(User.class);
                                    currentUser.setAvatar(files.get(0));
                                    currentUser.update(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if(e==null){
                                                T.showShort("修改成功");
                                                ImageUtils.setCircleBitmap(currentUser.getAvatar().getUrl(),ivUIcon );
                                            }else{
                                                T.showShort("修改失败");
                                            }
                                        }
                                    });
                                }
                            }
                        @Override
                        public void onError(int statuscode, String errormsg) {
                            T.showShort("错误码" + statuscode + ",错误描述：" + errormsg);
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                            //1、curIndex--表示当前第几个文件正在上传
                            //2、curPercent--表示当前上传文件的进度值（百分比）
                            //3、total--表示总的上传文件数
                            //4、totalPercent--表示总的上传进度（百分比）
//                            progressDialog.setProgress(totalPercent);
//                            L.e("------"+totalPercent);
                        }
                    });
                    break;
            }
        }
    }

}
