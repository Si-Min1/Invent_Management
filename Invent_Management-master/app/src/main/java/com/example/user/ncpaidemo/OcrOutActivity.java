package com.example.user.ncpaidemo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.kakao.usermgmt.response.model.User;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.ncpaidemo.MainActivity.setListViewHeightBasedOnChildren;

public class OcrOutActivity extends BaseActivity {

    //note UserItem List! 영수증 인식한 아이템 리스트
    private ArrayList<UserItem> list = new ArrayList<>();

    //note firebase 에서 조회한 아이템 리스트
    private ArrayList<UserItem> userItemArrayList = new ArrayList<>();

    //todo 조회되는/안되는 메뉴 리스트
    private ArrayList<Recipe> recipeArrayList = new ArrayList<>();

    private List<String> keyList;

    private ArrayList<Recipe> loadList;
    private final String DEFAULT = "DEFAULT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_ocr_out);

        Intent intent = getIntent();

        list = (ArrayList<UserItem>) intent.getSerializableExtra("userItem");  //영수증 아이템 리스트

        new FirebaseRecipeHelper().readRecipe(new FirebaseRecipeHelper.DataStatus() {
            @Override
            public void DataIsLoaded(ArrayList<Recipe> recipes, List<String> keys) {    //메뉴 리스트

                loadList = recipes;
                Recipe recipe = null;

                for (int i = 0; i < list.size(); i++) {
                    for (int j = 0; j < recipes.size(); j++) {
                        String lName = list.get(i).getName();
                        String rName = recipes.get(j).getName();

                        int count = list.get(i).getCount();
                        int price = list.get(i).getPrice();

                        if (lName.equals(rName)) {
                            ArrayList<RecipeItem> items = recipes.get(j).getUserItems();
                            int total_unit_price = recipes.get(j).getTotal_unit_price();
                            recipe = new Recipe(lName, price, total_unit_price, count, items);
                            break;
                        } else {
                            recipe = new Recipe(lName, price, 0, count, null);
                        }
                    }
                    recipeArrayList.add(recipe);
                }

                ListView listView = findViewById(R.id.ocr_out_list);
                OcrOutAdapter adapter = new OcrOutAdapter(OcrOutActivity.this, R.layout.content_ocr_out_list, recipeArrayList);
                listView.setAdapter(adapter);
                setListViewHeightBasedOnChildren(listView);

            }


            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });

        new FirebaseUserHelper().readUserItem(new FirebaseUserHelper.DataStatus() {
            @Override
            public void DataIsLoaded(ArrayList<UserItem> userItems, ArrayList<String> keys) {    //메뉴 리스트
                userItemArrayList = userItems;
                keyList = keys;
            }


            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });


        //note 상세입력 모두 입력후 확인 버튼
        findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean success = false;
                //todo 아이템이 있는 레시피에 들어가있는지 다시 조회
                for (int i = 0; i < recipeArrayList.size(); i++) {
                    for (int j = 0; j < loadList.size(); j++) {
                        if (recipeArrayList.get(i).getName().equals(loadList.get(j).getName())) {
                            recipeArrayList.get(i).setTotal_unit_price(loadList.get(j).getTotal_unit_price());
                            recipeArrayList.get(i).setUserItems(loadList.get(j).getUserItems());
                            break;
                        } else {
                            recipeArrayList.get(i).setTotal_unit_price(0);
                            recipeArrayList.get(i).setUserItems(null);
                        }
                    }
                    //todo null값/정수값 체크
                    if (recipeArrayList.get(i).getUserItems() == null) {
                        Toast.makeText(OcrOutActivity.this, "레시피에 없는 항목이 있습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    } else if (recipeArrayList.get(i).getCount() <= 0 || recipeArrayList.get(i).getPrice() <= 0) {
                        Toast.makeText(OcrOutActivity.this, "빈칸 혹은 잘못된 값이 입력되었습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (i == recipeArrayList.size()-1) {
                        success = true;
                    }
                }


                if (success) {
                    System.out.println("success!!!");

                    //for (int i = 0; i < recipeArrayList.size(); i++) {
                    //    recipeArrayList.get(i).print();
                    //}
                    ArrayList<UserItem> deleteList = new ArrayList<>();
                    ArrayList<UserItem> updateList = new ArrayList<>();

                    ArrayList<String> deleteKeys = new ArrayList<>();
                    ArrayList<String> updateKeys = new ArrayList<>();
                    int original = -1;
                    ArrayList<Integer> pos = new ArrayList<>();

                    for (int i = 0; i < recipeArrayList.size(); i++) {                                //레시피
                        for (int k = 0; k < recipeArrayList.get(i).getUserItems().size(); k++) {      //레시피 내 원재료
                            for (int j = 0; j < userItemArrayList.size(); j++) {                //파이어베이스 사용자 원재료

                                RecipeItem reItem = recipeArrayList.get(i).getUserItems().get(k);
                                UserItem userItem = userItemArrayList.get(j);

                                int rAmount;
                                if (original >= 0) {
                                    rAmount = reItem.getAmount();
                                    //rAmount = recipeArrayList.get(i).getUserItems().get(k).getAmount();
                                } else {
                                    rAmount = reItem.getAmount() * recipeArrayList.get(i).getCount();
                                    //rAmount = (recipeArrayList.get(i).getUserItems().get(k).getAmount()) * recipeArrayList.get(i).getCount();
                                }
                                //int uAmount = userItemArrayList.get(j).getAmount();
                                int uAmount = userItem.getAmount();

                                if (reItem.getsCategory().equals(userItem.getsCategory())&& !pos.contains(j)) {    //사용자 식재료 리스트 이름 == 레시피 리스트 이름


                                    if (userItem.getUnit().equals("kg") || userItem.getUnit().equals("L")) { //g이나 mL 로 바꾸기
                                        userItem.setAmount(uAmount * 1000);
                                        userItem.setUnit_amount(userItem.getUnit_amount() * 1000);
                                        if (userItem.getUnit().equals("kg"))
                                            userItem.setUnit("g");
                                        else userItem.setUnit("mL");
                                    }


                                    if (rAmount < uAmount) {
                                        int u_amount = uAmount - rAmount;
                                        int u_count;
                                        if ((uAmount - rAmount) % userItem.getUnit_amount() == 0) {
                                            u_count = (uAmount - rAmount) / userItem.getUnit_amount();
                                        } else {
                                            u_count = ((uAmount - rAmount) / userItem.getUnit_amount()) + 1;
                                        }

                                        userItem.setAmount(u_amount);
                                        userItem.setCount(u_count);
                                        updateList.add(userItem);
                                        updateKeys.add(keyList.get(j));

                                        if (original >= 0) {
                                            recipeArrayList.get(i).getUserItems().get(k).setAmount(original);
                                            original = -1;
                                        }
                                        //System.out.print("update1 : ");
                                        //System.out.println(updateList.get(i).getName() + " | " + updateList.get(j).getAmount() + " | " + updateList.get(j).getCount());
                                        break;

                                    } else if (rAmount >= uAmount) {
                                        original = recipeArrayList.get(i).getUserItems().get(k).getAmount();
                                        recipeArrayList.get(i).getUserItems().get(k).setAmount(rAmount - uAmount);
                                        deleteList.add(userItem);
                                        deleteKeys.add(keyList.get(j));
                                        //System.out.print("delete : ");
                                        //System.out.println(deleteList.get(i).getName() + " | " + deleteList.get(i).getAmount() + " | " + deleteList.get(j).getCount());
                                        //userItemArrayList.remove(userItem);
                                        pos.add(j);
                                        j =0;
                                    } else {
                                        System.out.println("I don't no why ! ");
                                    }
                                }

                            }
                        }
                    }

                    ArrayList<UserItem> undList = new ArrayList<>();

                    System.out.print("@@@@@@ DeleteList : ");
                    for (int i = 0; i < deleteList.size(); i++) {
                        undList.add(deleteList.get(i));
                        System.out.println(deleteList.get(i).getName() + " | " + deleteList.get(i).getAmount() + " | " + deleteList.get(i).getCount());
                        System.out.println("Key 이름 : " + deleteKeys.get(i));
                    }
                    System.out.print("@@@@@@ UpdateList : ");
                    for (int i = 0; i < updateList.size(); i++) {
                        undList.add(updateList.get(i));
                        System.out.println(updateList.get(i).getName() + " | " + updateList.get(i).getAmount() + " | " + updateList.get(i).getCount());
                        System.out.println("Key 이름 : " + updateKeys.get(i));
                    }

                    outList=undList;

                    for (int i = 0; i < updateList.size(); i++) {
                        new FirebaseUserHelper().updateUserItem(updateKeys.get(i), updateList.get(i), new FirebaseUserHelper.DataStatus() {
                            @Override
                            public void DataIsLoaded(ArrayList<UserItem> userItems, ArrayList<String> keys) {

                            }

                            @Override
                            public void DataIsInserted() {
                            }

                            @Override
                            public void DataIsUpdated() {
                                Toast.makeText(OcrOutActivity.this, "성공!", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void DataIsDeleted() {
                            }
                        });

                    }
                    for (int i = 0; i < deleteList.size(); i++) {
                        new FirebaseUserHelper().deleteUserItem(deleteKeys.get(i), new FirebaseUserHelper.DataStatus() {
                            @Override
                            public void DataIsLoaded(ArrayList<UserItem> userItems, ArrayList<String> keys) {
                            }

                            @Override
                            public void DataIsInserted() {
                            }

                            @Override
                            public void DataIsUpdated() {
                            }

                            @Override
                            public void DataIsDeleted() {
                                Toast.makeText(OcrOutActivity.this, "성공!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }

                }
            }
        });

    }

    void createNotificationChannel(String channelId, String channelName, int importance)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, importance));
        }
    }

    void createNotification(String channelId, int id, String title, String text, Intent intent)
    {
        PendingIntent pendingIntent = PendingIntent.getActivity(OcrOutActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(OcrOutActivity.this, channelId)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.title)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)    // 클릭시 설정된 PendingIntent가 실행된다
                .setAutoCancel(true)                // true이면 클릭시 알림이 삭제된다
                //.setTimeoutAfter(1000)
                //.setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
    }

    void destroyNotification(int id)
    {
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }

}
