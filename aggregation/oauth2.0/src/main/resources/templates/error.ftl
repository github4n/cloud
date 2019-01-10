<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no, shrink-to-fit=no">
    <meta name="theme-color" content="#000000">
    <title></title>
    <script>
        var dpr, rem;
        var docEl = document.documentElement;
        var fontEl = document.createElement('style');
        var scale = (docEl.clientWidth / 375).toFixed(2);
        dpr = window.devicePixelRatio || 1;
        //pad不放大
        if(scale > 1.4) {
            scale = 1;
        }
        rem = scale * 12;

        docEl.setAttribute('data-dpr', dpr);
        docEl.setAttribute('data-scale', scale);

        // 动态写入样式
        docEl.firstElementChild.appendChild(fontEl);
        fontEl.innerHTML = 'html{font-size:' + rem + 'px!important;}';
    </script>
    <style type="text/css">
        body {
            width: 100%;
            height: 100%;
            margin: 0;
            padding: 0;
        }
        .wrong-wrap {
            position: relative;
            width: 100vw;
            height: 100vh;
            background-color: #334054;
            overflow: hidden;
        }
        .wrong-wrap i {
            display: block;
            width: 5rem;
            height: 5rem;
            margin: 9rem auto 4rem;
            background-image: url(../oauth/img/common_icon_fail_120.png);
            background-size: cover;
            background-position: center;
        }
        .wrong-wrap p {
            width: 80%;
            text-align: center;
            margin: 0 auto 5rem;
            color: #fff;
        }
        .wrong-wrap .title {
            font-size: 20px;
        }
        .wrong-wrap .describe {
            font-size: 17px;
        }
        .wrong-wrap .button {
            display: block;
            position: absolute;
            right: 20%;
            bottom: 25%;
            width: 60%;
            height: 4.16rem;
            text-align: center;
            border-radius: 8px;
            font-size: 17px;
            line-height: 4.16rem;
            background-color: #53d192;
            color: white;
        }
        .wrong-wrap .button:active {
            opacity: .7;
        }
    </style>
</head>
<body>
<div class="wrong-wrap">
    <i></i>
    <p class="describe">${errorMsg}</p>
    <#if uuid?? && uuid?has_content>
        <a href="/oauth/authorize?uuid=${uuid!}" class="button">
            try again
        </a>
    </#if>
</div>
</body>
</html>
