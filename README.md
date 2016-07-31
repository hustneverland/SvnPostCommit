# Send A mail when svn posts commit

## what for

Send a mail when svn posts commit to your **Linux SVN server**.

## how to use 

1. Import this project to Eclipse(Java);
2. update config.properties, below is a template,
```xml
## send from who ?
send_from=temp@163.com
## the password for send_from to authenticate
mail_password=123456
## host of send_from
send_host=smtp.163.com
## send_to should be separated with comma
send_to=test@163.com
## path of your svn project
svn_project_path=/home/svn/myproject/
## send mail timeout, seconds
job_timeout_second=15
```
3. build & export to runnable jar(for example, **inv.jar**), and put the jar to your linux svn server(for example, your_svn_project/hooks/inv.jar);
4. rename your_svn_project/hooks/post-commit.impl to your_svn_project/hooks/post-commit, and add something like this,
```xml
export LANG=zh_CN.UTF-8
nohup java -jar your_svn_project/hooks/inv.jar > /dev/null 2>&1 &
exit 0
```
5. when svn posted commit, your will recv a mail like this,
```
Files Changed :
U   Documents/云平台/客户/04 千金/佛山千金实业有限公司终端安装信息表（20160612）.xlsx
D   Documents/云平台/客户/04 千金/千金设备信息
A   Documents/云平台/客户/04 千金/千金设备信息.txt
A   Documents/云平台/客户/04 千金/预警报警/千金监测点阀值20160724.xls

Changed Log :
千金资料更新

Newest version : 1442
```