# 基础模块
>##  <font face='微软雅黑' color='red' size='4'>注册</font>

>**请求URL：** 
- **<font face='微软雅黑' color='blue' size=4>http://127.0.0.1:80/smart_shoe_cabinet/api/account/register</font>**



>**请求方式：**
- post 

>**参数：** 

>|参数名|必选|类型|说明|默认值
>|----|---|----|-----|-----|
>|mobile|true|String|手机号码|13423423123|
>|password|true|String|密码|123456|
>|code|true|String|短信验证码|1234|
>|nick|true|String|用户昵称|大本本|



> **返回示例**

>     
>            {"code":0,"desc":"验证码不存在，请获取验证码"}
>     

> **返回参数说明** 

>| 参数名  | 说明 |
>|:-----|:-------------------------|



> **备注** 

>- 更多返回错误代码请看首页的错误代码描述

>##  <font face='微软雅黑' color='red' size='4'>登录</font>

>**请求URL：** 
- **<font face='微软雅黑' color='blue' size=4>http://127.0.0.1:80/smart_shoe_cabinet/api/account/login</font>**



>**请求方式：**
- post 

>**参数：** 

>|参数名|必选|类型|说明|默认值
>|----|---|----|-----|-----|
>|mobile|true|String|手机号码|13423423123|
>|password|true|String|密码|123456|
>|openId|false|String|微信openId 如果是商家端登录则不用传|12312|



> **返回示例**

>     
>            {"code":0,"desc":"账号不存在"}
>     

> **返回参数说明** 

>| 参数名  | 说明 |
>|:-----|:-------------------------|
>|topicOpen|是否开启默认人可以看朋友圈0否1是|
>|nick|昵称|
>|avatar|头像|
>|topicWallImg|链圈照片墙|



> **备注** 

>- 更多返回错误代码请看首页的错误代码描述

>##  <font face='微软雅黑' color='red' size='4'>获取验证码</font>

>**请求URL：** 
- **<font face='微软雅黑' color='blue' size=4>http://127.0.0.1:80/smart_shoe_cabinet/api/account/code</font>**



>**请求方式：**
- post 

>**参数：** 

>|参数名|必选|类型|说明|默认值
>|----|---|----|-----|-----|
>|mobile|true|String|手机号码|13423423123|
>|type|true|int|0注册1找回密码2绑定手机号3手机号登陆|3|



> **返回示例**

>     
>            {"code":1,"data":{"isNeedPsw":false},"desc":"发送成功"}
>     

> **返回参数说明** 

>| 参数名  | 说明 |
>|:-----|:-------------------------|
>|desc|默认验证码1234|
>|isNeedPsw|是否需要密码输入框（绑定手机号的时候使用）|



> **备注** 

>- 更多返回错误代码请看首页的错误代码描述

>##  <font face='微软雅黑' color='red' size='4'>绑定手机号</font>

>**请求URL：** 
- **<font face='微软雅黑' color='blue' size=4>http://127.0.0.1:80/smart_shoe_cabinet/api/account/bdMobile</font>**



>**请求方式：**
- post 

>**参数：** 

>|参数名|必选|类型|说明|默认值
>|----|---|----|-----|-----|
>|mobile|true|String|手机号码|13423423123|
>|code|true|int|手机验证码|1234|
>|openId|true|String|微信openId|123456|



> **返回示例**

>     
>            {"code":0,"data":false,"desc":"ids参数为空"}
>     

> **返回参数说明** 

>| 参数名  | 说明 |
>|:-----|:-------------------------|



> **备注** 

>- 更多返回错误代码请看首页的错误代码描述

>##  <font face='微软雅黑' color='red' size='4'>找回密码</font>

>**请求URL：** 
- **<font face='微软雅黑' color='blue' size=4>http://127.0.0.1:80/smart_shoe_cabinet/api/account/find</font>**



>**请求方式：**
- post 

>**参数：** 

>|参数名|必选|类型|说明|默认值
>|----|---|----|-----|-----|
>|mobile|true|String|手机号码|13423423123|
>|password|true|String|新密码|123456|
>|code|true|String|短信验证码|1234|



> **返回示例**

>     
>            {"code":0,"desc":"手机号码不正确，请重新发送验证码"}
>     

> **返回参数说明** 

>| 参数名  | 说明 |
>|:-----|:-------------------------|



> **备注** 

>- 更多返回错误代码请看首页的错误代码描述

>##  <font face='微软雅黑' color='red' size='4'>获取分享链接</font>

>**请求URL：** 
- **<font face='微软雅黑' color='blue' size=4>http://127.0.0.1:80/smart_shoe_cabinet/api/account/getShareUrl</font>**



>**请求方式：**
- post 

>**参数：** 

>|参数名|必选|类型|说明|默认值
>|----|---|----|-----|-----|
>|url|true|String|前端当前页面的url(不需要#号后面的参数)|http://www.baidu.com/html/index.html|



> **返回示例**

>     
>            {"code":1,"data":{"signature":"8eae8ae2b6ff17be086ee3af42d46e2876df03d6","noncestr":"1553756005731","timestamp":"1553756005"},"desc":"获取成功"}
>     

> **返回参数说明** 

>| 参数名  | 说明 |
>|:-----|:-------------------------|



> **备注** 

>- 更多返回错误代码请看首页的错误代码描述

>##  <font face='微软雅黑' color='red' size='4'>退出登录</font>

>**请求URL：** 
- **<font face='微软雅黑' color='blue' size=4>http://127.0.0.1:80/smart_shoe_cabinet/api/account/logout</font>**



>**请求方式：**
- post 

>**参数：** 

>|参数名|必选|类型|说明|默认值
>|----|---|----|-----|-----|
>|sessionId|true|String|个人信息唯一标识ID|2e40f29dbed24774a3326146af3212cb|



> **返回示例**

>     
>            {"code":0,"desc":"退出失败"}
>     

> **返回参数说明** 

>| 参数名  | 说明 |
>|:-----|:-------------------------|



> **备注** 

>- 更多返回错误代码请看首页的错误代码描述

>##  <font face='微软雅黑' color='red' size='4'>修改密码</font>

>**请求URL：** 
- **<font face='微软雅黑' color='blue' size=4>http://127.0.0.1:80/smart_shoe_cabinet/api/account/updatePwd</font>**



>**请求方式：**
- post 

>**参数：** 

>|参数名|必选|类型|说明|默认值
>|----|---|----|-----|-----|
>|sessionId|true|String|个人信息唯一标识ID|2e40f29dbed24774a3326146af3212cb|
>|oldPwd|true|String|旧密码|123456|
>|newPwd|true|String|新密码|12345678|



> **返回示例**

>     
>            {"code":2,"desc":"用户不存在"}
>     

> **返回参数说明** 

>| 参数名  | 说明 |
>|:-----|:-------------------------|



> **备注** 

>- 更多返回错误代码请看首页的错误代码描述

>##  <font face='微软雅黑' color='red' size='4'>手机验证码登录</font>

>**请求URL：** 
- **<font face='微软雅黑' color='blue' size=4>http://127.0.0.1:80/smart_shoe_cabinet/api/account/loginByMobileCode</font>**



>**请求方式：**
- post 

>**参数：** 

>|参数名|必选|类型|说明|默认值
>|----|---|----|-----|-----|
>|mobile|true|String|手机号码|13423423123|
>|code|true|int|短信验证码|1234|
>|openId|true|String|微信openId|123|



> **返回示例**

>     
>            {"code":0,"desc":"发生错误"}
>     

> **返回参数说明** 

>| 参数名  | 说明 |
>|:-----|:-------------------------|



> **备注** 

>- 更多返回错误代码请看首页的错误代码描述

# 信息模块
>##  <font face='微软雅黑' color='red' size='4'>获取个人信息</font>

>**请求URL：** 
- **<font face='微软雅黑' color='blue' size=4>http://127.0.0.1:80/smart_shoe_cabinet/api/account/user/getUserInfo</font>**



>**请求方式：**
- post 

>**参数：** 

>|参数名|必选|类型|说明|默认值
>|----|---|----|-----|-----|
>|sessionId|true|String|个人信息唯一标识ID|2e40f29dbed24774a3326146af3212cb|



> **返回示例**

>     
>            {"code":2,"desc":"用户不存在"}
>     

> **返回参数说明** 

>| 参数名  | 说明 |
>|:-----|:-------------------------|
>|nick|昵称|
>|head|头像|



> **备注** 

>- 更多返回错误代码请看首页的错误代码描述

>##  <font face='微软雅黑' color='red' size='4'>修改个人信息</font>

>**请求URL：** 
- **<font face='微软雅黑' color='blue' size=4>http://127.0.0.1:80/smart_shoe_cabinet/api/account/user/updateInfo</font>**



>**请求方式：**
- post 

>**参数：** 

>|参数名|必选|类型|说明|默认值
>|----|---|----|-----|-----|
>|sessionId|true|String|个人信息唯一标识ID|2e40f29dbed24774a3326146af3212cb|
>|avatar|false|file|头像(不传不修改)|D://testimg1.jpg|
>|nick|false|String|昵称(不传不修改)|Jayson|
>|sex|false|int|性别0男1女(不传不修改)|0|



> **返回示例**

>     
>            
>     

> **返回参数说明** 

>| 参数名  | 说明 |
>|:-----|:-------------------------|
>|data|最新的个人信息|



> **备注** 

>- 更多返回错误代码请看首页的错误代码描述

# 版本模块
>##  <font face='微软雅黑' color='red' size='4'>获取安卓最新版本信息</font>

>**请求URL：** 
- **<font face='微软雅黑' color='blue' size=4>http://127.0.0.1:80/smart_shoe_cabinet/api/app/version/getNewAndroid</font>**



>**请求方式：**
- post 

>**参数：** 

>|参数名|必选|类型|说明|默认值
>|----|---|----|-----|-----|



> **返回示例**

>     
>            
>     

> **返回参数说明** 

>| 参数名  | 说明 |
>|:-----|:-------------------------|



> **备注** 

>- 更多返回错误代码请看首页的错误代码描述

>##  <font face='微软雅黑' color='red' size='4'>获取ios最新版本信息</font>

>**请求URL：** 
- **<font face='微软雅黑' color='blue' size=4>http://127.0.0.1:80/smart_shoe_cabinet/api/app/version/getNewIos</font>**



>**请求方式：**
- post 

>**参数：** 

>|参数名|必选|类型|说明|默认值
>|----|---|----|-----|-----|



> **返回示例**

>     
>            
>     

> **返回参数说明** 

>| 参数名  | 说明 |
>|:-----|:-------------------------|



> **备注** 

>- 更多返回错误代码请看首页的错误代码描述

# 地区数据模块
>##  <font face='微软雅黑' color='red' size='4'>获取所有区域</font>

>**请求URL：** 
- **<font face='微软雅黑' color='blue' size=4>http://127.0.0.1:80/smart_shoe_cabinet/api/area/getAll</font>**



>**请求方式：**
- post 

>**参数：** 

>|参数名|必选|类型|说明|默认值
>|----|---|----|-----|-----|



> **返回示例**

>     
>            
>     

> **返回参数说明** 

>| 参数名  | 说明 |
>|:-----|:-------------------------|



> **备注** 

>- 更多返回错误代码请看首页的错误代码描述

>##  <font face='微软雅黑' color='red' size='4'>省区域列表</font>

>**请求URL：** 
- **<font face='微软雅黑' color='blue' size=4>http://127.0.0.1:80/smart_shoe_cabinet/api/area/getByShengList</font>**



>**请求方式：**
- post 

>**参数：** 

>|参数名|必选|类型|说明|默认值
>|----|---|----|-----|-----|



> **返回示例**

>     
>            
>     

> **返回参数说明** 

>| 参数名  | 说明 |
>|:-----|:-------------------------|



> **备注** 

>- 更多返回错误代码请看首页的错误代码描述

>##  <font face='微软雅黑' color='red' size='4'>市区域列表</font>

>**请求URL：** 
- **<font face='微软雅黑' color='blue' size=4>http://127.0.0.1:80/smart_shoe_cabinet/api/area/getByShiList</font>**



>**请求方式：**
- post 

>**参数：** 

>|参数名|必选|类型|说明|默认值
>|----|---|----|-----|-----|
>|parentId|true|int|省id|110000|



> **返回示例**

>     
>            {"code":1,"data":[{"zipcode":"100000","pinyin":"Beijing","citycode":"010","lng":"116.405285","fz":0,"name":"北京市","leveltype":2,"id":110100,"shortname":"北京","parentid":110000,"lat":"39.904989"}],"desc":"获取成功"}
>     

> **返回参数说明** 

>| 参数名  | 说明 |
>|:-----|:-------------------------|



> **备注** 

>- 更多返回错误代码请看首页的错误代码描述

>##  <font face='微软雅黑' color='red' size='4'>区区域列表</font>

>**请求URL：** 
- **<font face='微软雅黑' color='blue' size=4>http://127.0.0.1:80/smart_shoe_cabinet/api/area/getByQuList</font>**



>**请求方式：**
- post 

>**参数：** 

>|参数名|必选|类型|说明|默认值
>|----|---|----|-----|-----|
>|parentId|true|int|市id|110100|



> **返回示例**

>     
>            {"code":1,"data":[{"zipcode":"102100","pinyin":"Yanqing","citycode":"010","lng":"115.97494","fz":0,"name":"延庆县","leveltype":3,"id":110229,"shortname":"延庆","parentid":110100,"lat":"40.45672"},{"zipcode":"101500","pinyin":"Miyun","citycode":"010","lng":"116.84295","fz":0,"name":"密云县","leveltype":3,"id":110228,"shortname":"密云","parentid":110100,"lat":"40.37618"},{"zipcode":"101200","pinyin":"Pinggu","citycode":"010","lng":"117.12133","fz":0,"name":"平谷区","leveltype":3,"id":110117,"shortname":"平谷","parentid":110100,"lat":"40.14056"},{"zipcode":"101400","pinyin":"Huairou","citycode":"010","lng":"116.63168","fz":0,"name":"怀柔区","leveltype":3,"id":110116,"shortname":"怀柔","parentid":110100,"lat":"40.31602"},{"zipcode":"102600","pinyin":"Daxing","citycode":"010","lng":"116.34149","fz":0,"name":"大兴区","leveltype":3,"id":110115,"shortname":"大兴","parentid":110100,"lat":"39.72668"},{"zipcode":"102200","pinyin":"Changping","citycode":"010","lng":"116.2312","fz":0,"name":"昌平区","leveltype":3,"id":110114,"shortname":"昌平","parentid":110100,"lat":"40.22072"},{"zipcode":"101300","pinyin":"Shunyi","citycode":"010","lng":"116.65417","fz":0,"name":"顺义区","leveltype":3,"id":110113,"shortname":"顺义","parentid":110100,"lat":"40.1302"},{"zipcode":"101149","pinyin":"Tongzhou","citycode":"010","lng":"116.65716","fz":0,"name":"通州区","leveltype":3,"id":110112,"shortname":"通州","parentid":110100,"lat":"39.90966"},{"zipcode":"102488","pinyin":"Fangshan","citycode":"010","lng":"116.14257","fz":0,"name":"房山区","leveltype":3,"id":110111,"shortname":"房山","parentid":110100,"lat":"39.74786"},{"zipcode":"102300","pinyin":"Mentougou","citycode":"010","lng":"116.10137","fz":0,"name":"门头沟区","leveltype":3,"id":110109,"shortname":"门头沟","parentid":110100,"lat":"39.94043"},{"zipcode":"100089","pinyin":"Haidian","citycode":"010","lng":"116.29812","fz":0,"name":"海淀区","leveltype":3,"id":110108,"shortname":"海淀","parentid":110100,"lat":"39.95931"},{"zipcode":"100043","pinyin":"Shijingshan","citycode":"010","lng":"116.2229","fz":0,"name":"石景山区","leveltype":3,"id":110107,"shortname":"石景山","parentid":110100,"lat":"39.90564"},{"zipcode":"100071","pinyin":"Fengtai","citycode":"010","lng":"116.28625","fz":0,"name":"丰台区","leveltype":3,"id":110106,"shortname":"丰台","parentid":110100,"lat":"39.8585"},{"zipcode":"100020","pinyin":"Chaoyang","citycode":"010","lng":"116.48548","fz":0,"name":"朝阳区","leveltype":3,"id":110105,"shortname":"朝阳","parentid":110100,"lat":"39.9484"},{"zipcode":"100032","pinyin":"Xicheng","citycode":"010","lng":"116.36003","fz":0,"name":"西城区","leveltype":3,"id":110102,"shortname":"西城","parentid":110100,"lat":"39.9305"},{"zipcode":"100010","pinyin":"Dongcheng","citycode":"010","lng":"116.41005","fz":0,"name":"东城区","leveltype":3,"id":110101,"shortname":"东城","parentid":110100,"lat":"39.93157"}],"desc":"获取成功"}
>     

> **返回参数说明** 

>| 参数名  | 说明 |
>|:-----|:-------------------------|



> **备注** 

>- 更多返回错误代码请看首页的错误代码描述

# 系统信息模块
>##  <font face='微软雅黑' color='red' size='4'>获取系统文本信息</font>

>**请求URL：** 
- **<font face='微软雅黑' color='blue' size=4>http://127.0.0.1:80/smart_shoe_cabinet/api/content/get</font>**



>**请求方式：**
- post 

>**参数：** 

>|参数名|必选|类型|说明|默认值
>|----|---|----|-----|-----|
>|id|true|int|1：服务协议，2：隐私政策，3:运费规则，4|2|



> **返回示例**

>     
>            {"code":1,"data":{"createTime":"2019-01-18 19:00:48","name":"关于我们","id":2,"type":0,"content":"<p>关于本港台</p>"},"desc":"获取成功"}
>     

> **返回参数说明** 

>| 参数名  | 说明 |
>|:-----|:-------------------------|
>|type|0H5文本 1纯文本|



> **备注** 

>- 更多返回错误代码请看首页的错误代码描述

