from bs4 import BeautifulSoup

# 目标HTML代码片段
html_fragment = '''
<div class="jigsaw" id="walBox">
        <div class="item half oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 442.179px;"> <img
                data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202209/e2fdd69e2fb6facee2cdabefceb7c13b.jpg"
                alt="美女,清纯,可爱" title="关键字：美女,清纯,可爱"
                data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202209/e2fdd69e2fb6facee2cdabefceb7c13b.jpg"
                src="./美女模特 - 在线壁纸_files/e2fdd69e2fb6facee2cdabefceb7c13b.jpg" style="display: inline;"></div>
        <div class="item quater" style="height: 442.179px;">
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202209/1d9e79967a0e9ef1bb6869f8d2d27d56.jpg"
                    alt="美女,可爱,小清新" title="关键字：美女,可爱,小清新"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202209/1d9e79967a0e9ef1bb6869f8d2d27d56.jpg"
                    src="./美女模特 - 在线壁纸_files/1d9e79967a0e9ef1bb6869f8d2d27d56.jpg" style="display: inline;"> </div>
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202209/bec005059c961095de116266433609ad.jpg"
                    alt="美女,性感,和服" title="关键字：美女,性感,和服"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202209/bec005059c961095de116266433609ad.jpg"
                    src="./美女模特 - 在线壁纸_files/bec005059c961095de116266433609ad.jpg" style="display: inline;"> </div>
        </div>
        <div class="item quater" style="height: 442.179px;">
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202209/87b77e03d0965e8925f7a22e4ec0bc04.jpg"
                    alt="美女,可爱,小清新" title="关键字：美女,可爱,小清新"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202209/87b77e03d0965e8925f7a22e4ec0bc04.jpg"
                    src="./美女模特 - 在线壁纸_files/87b77e03d0965e8925f7a22e4ec0bc04.jpg" style="display: inline;">
                <ul class="down" title="下载壁纸">
                    <li><a href="http://image.baidu.com/search/down?tn=download&amp;word=download&amp;ie=utf8&amp;fr=detail&amp;url=http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202209/87b77e03d0965e8925f7a22e4ec0bc04.jpg"
                            target="_blank" title="下载原始尺寸图片">原始尺寸</a></li>
                </ul>
            </div>
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202209/9bc0885c331c4a498efe07f93d4ff301.jpg"
                    alt="美女,可爱,小清新" title="关键字：美女,可爱,小清新"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202209/9bc0885c331c4a498efe07f93d4ff301.jpg"
                    src="./美女模特 - 在线壁纸_files/9bc0885c331c4a498efe07f93d4ff301.jpg" style="display: inline;">
                <ul class="down" title="下载壁纸">
                    <li><a href="http://image.baidu.com/search/down?tn=download&amp;word=download&amp;ie=utf8&amp;fr=detail&amp;url=http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202209/9bc0885c331c4a498efe07f93d4ff301.jpg"
                            target="_blank" title="下载原始尺寸图片">原始尺寸</a></li>
                </ul>
            </div>
        </div>
        <div class="item half oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 442.179px;"> <img
                data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202209/143f603b00bb32770c08091ada758d2f.jpg"
                alt="古风,美女,汉服" title="关键字：古风,美女,汉服"
                data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202209/143f603b00bb32770c08091ada758d2f.jpg"
                src="./美女模特 - 在线壁纸_files/143f603b00bb32770c08091ada758d2f.jpg" style="display: inline;"></div>
        <div class="item quater" style="height: 442.179px;">
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202209/aa4e249d75dfa47bf508744f0e0291d0.jpg"
                    alt="美女,古风,汉服" title="关键字：美女,古风,汉服"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202209/aa4e249d75dfa47bf508744f0e0291d0.jpg"
                    src="./美女模特 - 在线壁纸_files/aa4e249d75dfa47bf508744f0e0291d0.jpg" style="display: inline;"> </div>
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202209/4516b4751bca0a27c94bcf3ab1fcb645.jpg"
                    alt="美女,可爱,古风,汉服" title="关键字：美女,可爱,古风,汉服"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202209/4516b4751bca0a27c94bcf3ab1fcb645.jpg"
                    src="./美女模特 - 在线壁纸_files/4516b4751bca0a27c94bcf3ab1fcb645.jpg" style="display: inline;"> </div>
        </div>
        <div class="item quater" style="height: 442.179px;">
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202209/564ea17d70d3d4857c08e8c23d43717f.jpg"
                    alt="美女,模特,古风,汉服" title="关键字：美女,模特,古风,汉服"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202209/564ea17d70d3d4857c08e8c23d43717f.jpg"
                    src="./美女模特 - 在线壁纸_files/564ea17d70d3d4857c08e8c23d43717f.jpg" style="display: inline;">
                <ul class="down" title="下载壁纸">
                    <li><a href="http://image.baidu.com/search/down?tn=download&amp;word=download&amp;ie=utf8&amp;fr=detail&amp;url=http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202209/564ea17d70d3d4857c08e8c23d43717f.jpg"
                            target="_blank" title="下载原始尺寸图片">原始尺寸</a></li>
                </ul>
            </div>
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202209/667be4da89e39122a909eba46da83f04.jpg"
                    alt="美女,古风,古装,汉服" title="关键字：美女,古风,古装,汉服"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202209/667be4da89e39122a909eba46da83f04.jpg"
                    src="./美女模特 - 在线壁纸_files/667be4da89e39122a909eba46da83f04.jpg" style="display: inline;">
                <ul class="down" title="下载壁纸">
                    <li><a href="http://image.baidu.com/search/down?tn=download&amp;word=download&amp;ie=utf8&amp;fr=detail&amp;url=http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202209/667be4da89e39122a909eba46da83f04.jpg"
                            target="_blank" title="下载原始尺寸图片">原始尺寸</a></li>
                </ul>
            </div>
        </div>
        <div class="item quater" style="height: 442.179px;">
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202312/b4822f90d755a44118dc2dcf1e9d6215--1364187999.jpg"
                    alt="清纯,冬天,回眸" title="关键字：清纯,冬天,回眸"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202312/b4822f90d755a44118dc2dcf1e9d6215--1364187999.jpg"
                    src="./美女模特 - 在线壁纸_files/b4822f90d755a44118dc2dcf1e9d6215--1364187999.jpg" style="display: inline;">
            </div>
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202312/5d8f7f84124a73bea4c2fa520a86446b--761198732.jpg"
                    alt="文艺古风,小船,江南" title="关键字：文艺古风,小船,江南"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202312/5d8f7f84124a73bea4c2fa520a86446b--761198732.jpg"
                    src="./美女模特 - 在线壁纸_files/5d8f7f84124a73bea4c2fa520a86446b--761198732.jpg" style="display: inline;">
            </div>
        </div>
        <div class="item half oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 442.179px;"> <img
                data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202310/d8fca299f743ab958ce6fd34524bb76d--1574323644.jpg"
                alt="长发,照相机,拍照" title="关键字：长发,照相机,拍照"
                data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202310/d8fca299f743ab958ce6fd34524bb76d--1574323644.jpg"
                src="./美女模特 - 在线壁纸_files/d8fca299f743ab958ce6fd34524bb76d--1574323644.jpg" style="display: inline;">
        </div>
        <div class="item quater" style="height: 442.179px;">
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202310/309587e09cb172d58c2436acb1943460--2623743198.jpg"
                    alt="美女,清纯" title="关键字：美女,清纯"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202310/309587e09cb172d58c2436acb1943460--2623743198.jpg"
                    src="./美女模特 - 在线壁纸_files/309587e09cb172d58c2436acb1943460--2623743198.jpg" style="display: inline;">
                <ul class="down" title="下载壁纸">
                    <li><a href="http://image.baidu.com/search/down?tn=download&amp;word=download&amp;ie=utf8&amp;fr=detail&amp;url=http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202310/309587e09cb172d58c2436acb1943460--2623743198.jpg"
                            target="_blank" title="下载原始尺寸图片">原始尺寸</a></li>
                </ul>
            </div>
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202310/01b4b14fe111b6dcf41fad84c4232c11--1899435386.jpg"
                    alt="美女,清纯" title="关键字：美女,清纯"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202310/01b4b14fe111b6dcf41fad84c4232c11--1899435386.jpg"
                    src="./美女模特 - 在线壁纸_files/01b4b14fe111b6dcf41fad84c4232c11--1899435386.jpg" style="display: inline;">
                <ul class="down" title="下载壁纸">
                    <li><a href="http://image.baidu.com/search/down?tn=download&amp;word=download&amp;ie=utf8&amp;fr=detail&amp;url=http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202310/01b4b14fe111b6dcf41fad84c4232c11--1899435386.jpg"
                            target="_blank" title="下载原始尺寸图片">原始尺寸</a></li>
                </ul>
            </div>
        </div>
        <div class="item quater" style="height: 442.179px;">
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202309/6af2e4a9868f4c423395466a1ee9403b--2190949263.jpg"
                    alt="清纯,羞涩,刘海," title="关键字：清纯,羞涩,刘海,"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202309/6af2e4a9868f4c423395466a1ee9403b--2190949263.jpg"
                    src="./美女模特 - 在线壁纸_files/6af2e4a9868f4c423395466a1ee9403b--2190949263.jpg" style="display: inline;">
            </div>
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202309/62257519ea1f70061f7ae2354df9c593--2839199481.jpg"
                    alt="彩色头发,彩虹美女,长发,回望" title="关键字：彩色头发,彩虹美女,长发,回望"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202309/62257519ea1f70061f7ae2354df9c593--2839199481.jpg"
                    src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsQAAA7EAZUrDhsAAAANSURBVBhXYzh8+PB/AAffA0nNPuCLAAAAAElFTkSuQmCC">
                <ul class="down" title="下载壁纸">
                    <li><a href="http://image.baidu.com/search/down?tn=download&amp;word=download&amp;ie=utf8&amp;fr=detail&amp;url=http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202309/62257519ea1f70061f7ae2354df9c593--2839199481.jpg"
                            target="_blank" title="下载原始尺寸图片">原始尺寸</a></li>
                </ul>
            </div>
        </div>
        <div class="item quater" style="height: 442.179px;">
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202309/eb9a58ebc1e988c48f0dec969e94e1ca--1690003083.jpg"
                    alt="清纯,长发,女孩,回眸" title="关键字：清纯,长发,女孩,回眸"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202309/eb9a58ebc1e988c48f0dec969e94e1ca--1690003083.jpg"
                    src="./美女模特 - 在线壁纸_files/eb9a58ebc1e988c48f0dec969e94e1ca--1690003083.jpg" style="display: inline;">
            </div>
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202309/660261f20167e40fd5d9404f40eea64a--3687808542.jpg"
                    alt="街头,微笑,长发" title="关键字：街头,微笑,长发"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202309/660261f20167e40fd5d9404f40eea64a--3687808542.jpg"
                    src="./美女模特 - 在线壁纸_files/660261f20167e40fd5d9404f40eea64a--3687808542.jpg" style="display: inline;">
                <ul class="down" title="下载壁纸">
                    <li><a href="http://image.baidu.com/search/down?tn=download&amp;word=download&amp;ie=utf8&amp;fr=detail&amp;url=http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202309/660261f20167e40fd5d9404f40eea64a--3687808542.jpg"
                            target="_blank" title="下载原始尺寸图片">原始尺寸</a></li>
                </ul>
            </div>
        </div>
        <div class="item half oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 442.179px;"> <img
                data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202309/f5d98998a4b3cb3853226340ba9fc125--1311198732.jpg"
                alt="街头,长发女孩,气质,阳光" title="关键字：街头,长发女孩,气质,阳光"
                data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202309/f5d98998a4b3cb3853226340ba9fc125--1311198732.jpg"
                src="./美女模特 - 在线壁纸_files/f5d98998a4b3cb3853226340ba9fc125--1311198732.jpg" style="display: inline;">
            <ul class="down" title="下载壁纸">
                <li><a href="http://image.baidu.com/search/down?tn=download&amp;word=download&amp;ie=utf8&amp;fr=detail&amp;url=http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202309/f5d98998a4b3cb3853226340ba9fc125--1311198732.jpg"
                        target="_blank" title="下载原始尺寸图片">原始尺寸</a></li>
            </ul>
        </div>
        <div class="item quater" style="height: 442.179px;">
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202309/2ac585d590c317d0525edb21cccf5da3--2607229340.jpg"
                    alt="美女,少女,性感,AI,清新" title="关键字：美女,少女,性感,AI,清新"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202309/2ac585d590c317d0525edb21cccf5da3--2607229340.jpg"
                    src="./美女模特 - 在线壁纸_files/2ac585d590c317d0525edb21cccf5da3--2607229340.jpg" style="display: inline;">
            </div>
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202309/b3e31520281ecea15c6b435e84c57cb0--3137672546.jpg"
                    alt="美女,少女,性感AI清新" title="关键字：美女,少女,性感AI清新"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202309/b3e31520281ecea15c6b435e84c57cb0--3137672546.jpg"
                    src="./美女模特 - 在线壁纸_files/b3e31520281ecea15c6b435e84c57cb0--3137672546.jpg" style="display: inline;">
            </div>
        </div>
        <div class="item quater" style="height: 442.179px;">
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202308/d5e31d983d09e13910c766dbddabeffe--1449971930.jpg"
                    alt="性感女神,侧躺,居家" title="关键字：性感女神,侧躺,居家"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202308/d5e31d983d09e13910c766dbddabeffe--1449971930.jpg"
                    src="./美女模特 - 在线壁纸_files/d5e31d983d09e13910c766dbddabeffe--1449971930.jpg" style="display: inline;">
            </div>
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202306/4aea1d59562ed6bd21accaea3d0cdb4d--406180850.jpg"
                    alt="文艺古风,西域,沙" title="关键字：文艺古风,西域,沙"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202306/4aea1d59562ed6bd21accaea3d0cdb4d--406180850.jpg"
                    src="./美女模特 - 在线壁纸_files/4aea1d59562ed6bd21accaea3d0cdb4d--406180850.jpg" style="display: inline;">
            </div>
        </div>
        <div class="item half oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 442.179px;"> <img
                data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202302/64b0b8134fc276e6198e06949ed6969e--525150279.jpg"
                alt="女明星,刘亦菲,马,去有风的地方" title="关键字：女明星,刘亦菲,马,去有风的地方"
                data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202302/64b0b8134fc276e6198e06949ed6969e--525150279.jpg"
                src="./美女模特 - 在线壁纸_files/64b0b8134fc276e6198e06949ed6969e--525150279.jpg" style="display: inline;">
            <ul class="down" title="下载壁纸">
                <li><a href="http://image.baidu.com/search/down?tn=download&amp;word=download&amp;ie=utf8&amp;fr=detail&amp;url=http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202302/64b0b8134fc276e6198e06949ed6969e--525150279.jpg"
                        target="_blank" title="下载原始尺寸图片">原始尺寸</a></li>
            </ul>
        </div>
        <div class="item half oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 442.179px;"> <img
                data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202212/4da96bac586a995c1559f40c1b36b235--1909625720.jpg"
                alt="欧美女神,金发,帽子,冬季" title="关键字：欧美女神,金发,帽子,冬季"
                data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202212/4da96bac586a995c1559f40c1b36b235--1909625720.jpg"
                src="./美女模特 - 在线壁纸_files/4da96bac586a995c1559f40c1b36b235--1909625720.jpg" style="display: inline;">
        </div>
        <div class="item quater" style="height: 442.179px;">
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202212/9172985a69241d260240d8fa012728c3--1407122610.jpg"
                    alt="欧美女神,服务员,咖啡店" title="关键字：欧美女神,服务员,咖啡店"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202212/9172985a69241d260240d8fa012728c3--1407122610.jpg"
                    src="./美女模特 - 在线壁纸_files/9172985a69241d260240d8fa012728c3--1407122610.jpg" style="display: inline;">
            </div>
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202211/1908da2b1afec00022ad3b22c02da692--4079323895.jpg"
                    alt="性感女神,苗条,足球宝贝" title="关键字：性感女神,苗条,足球宝贝"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202211/1908da2b1afec00022ad3b22c02da692--4079323895.jpg"
                    src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsQAAA7EAZUrDhsAAAANSURBVBhXYzh8+PB/AAffA0nNPuCLAAAAAElFTkSuQmCC">
            </div>
        </div>
        <div class="item quater" style="height: 442.179px;">
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202211/dc954cdfc5aac06f8e26c1cdc6d02349--1494553420.jpg"
                    alt="古风文艺,雨伞,红衣女子,古装" title="关键字：古风文艺,雨伞,红衣女子,古装"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202211/dc954cdfc5aac06f8e26c1cdc6d02349--1494553420.jpg"
                    src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsQAAA7EAZUrDhsAAAANSURBVBhXYzh8+PB/AAffA0nNPuCLAAAAAElFTkSuQmCC">
            </div>
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202211/30d74773eeb77bfc4cf9db10e62a80f2--3057522847.jpg"
                    alt="居家,清纯,养眼" title="关键字：居家,清纯,养眼"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202211/30d74773eeb77bfc4cf9db10e62a80f2--3057522847.jpg"
                    src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsQAAA7EAZUrDhsAAAANSURBVBhXYzh8+PB/AAffA0nNPuCLAAAAAElFTkSuQmCC">
            </div>
        </div>
        <div class="item half oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 442.179px;"> <img
                data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202210/b07fa9c46502bbb1d715a7034a3a7c42--77324329.jpg"
                alt="文艺古风,古装,瀑布" title="关键字：文艺古风,古装,瀑布"
                data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202210/b07fa9c46502bbb1d715a7034a3a7c42--77324329.jpg"
                src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsQAAA7EAZUrDhsAAAANSURBVBhXYzh8+PB/AAffA0nNPuCLAAAAAElFTkSuQmCC">
        </div>
        <div class="item quater" style="height: 442.179px;">
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202208/40f1faa375cb09993ac56d0100247169--1155426306.jpg"
                    alt="COS,兽耳,指甲,戒指" title="关键字：COS,兽耳,指甲,戒指"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202208/40f1faa375cb09993ac56d0100247169--1155426306.jpg"
                    src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsQAAA7EAZUrDhsAAAANSURBVBhXYzh8+PB/AAffA0nNPuCLAAAAAElFTkSuQmCC">
            </div>
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202208/15383340a19d5e66858afec909e8376a--143226042.jpg"
                    alt="清纯,民族,耳环,大眼女孩" title="关键字：清纯,民族,耳环,大眼女孩"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202208/15383340a19d5e66858afec909e8376a--143226042.jpg"
                    src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsQAAA7EAZUrDhsAAAANSURBVBhXYzh8+PB/AAffA0nNPuCLAAAAAElFTkSuQmCC">
            </div>
        </div>
        <div class="item quater" style="height: 442.179px;">
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202208/f80a3cf13ed922ae56c295b6e1750b9f--862862084.jpg"
                    alt="大眼睛,女孩,长发,回望" title="关键字：大眼睛,女孩,长发,回望"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202208/f80a3cf13ed922ae56c295b6e1750b9f--862862084.jpg"
                    src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsQAAA7EAZUrDhsAAAANSURBVBhXYzh8+PB/AAffA0nNPuCLAAAAAElFTkSuQmCC">
            </div>
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202208/03e3215ba41e9dda4b612d435dc33749--4017768935.jpg"
                    alt="清纯,COS,兽耳,可爱" title="关键字：清纯,COS,兽耳,可爱"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202208/03e3215ba41e9dda4b612d435dc33749--4017768935.jpg"
                    src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsQAAA7EAZUrDhsAAAANSURBVBhXYzh8+PB/AAffA0nNPuCLAAAAAElFTkSuQmCC">
            </div>
        </div>
        <div class="item quater" style="height: 442.179px;">
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202208/e46db6533224e5f49e5a0187c46f5f74--583756240.jpg"
                    alt="欧美女神,短发,回眸,白云" title="关键字：欧美女神,短发,回眸,白云"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202208/e46db6533224e5f49e5a0187c46f5f74--583756240.jpg"
                    src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsQAAA7EAZUrDhsAAAANSURBVBhXYzh8+PB/AAffA0nNPuCLAAAAAElFTkSuQmCC">
            </div>
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202208/c052bcaad5831e744d80408c7f18309e--1643858281.jpg"
                    alt="清纯,手指,嘴唇,碎发" title="关键字：清纯,手指,嘴唇,碎发"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202208/c052bcaad5831e744d80408c7f18309e--1643858281.jpg"
                    src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsQAAA7EAZUrDhsAAAANSURBVBhXYzh8+PB/AAffA0nNPuCLAAAAAElFTkSuQmCC">
            </div>
        </div>
        <div class="item quater" style="height: 442.179px;">
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202208/097af3d9e33c26ed741a8df79445f2e9--3623425165.jpg"
                    alt="架子鼓,吉他,清纯,可爱短发" title="关键字：架子鼓,吉他,清纯,可爱短发"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202208/097af3d9e33c26ed741a8df79445f2e9--3623425165.jpg"
                    src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsQAAA7EAZUrDhsAAAANSURBVBhXYzh8+PB/AAffA0nNPuCLAAAAAElFTkSuQmCC">
            </div>
            <div class="Hhalf oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 221.089px;"> <img
                    data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202208/fb48e4405d33675142f1f96bf5d13433--1464361656.jpg"
                    alt="欧美女神,金发,海浪,海滩,毛衣" title="关键字：欧美女神,金发,海浪,海滩,毛衣"
                    data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202208/fb48e4405d33675142f1f96bf5d13433--1464361656.jpg"
                    src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsQAAA7EAZUrDhsAAAANSURBVBhXYzh8+PB/AAffA0nNPuCLAAAAAElFTkSuQmCC">
            </div>
        </div>
        <div class="item half oneImg" onmouseover="hoverJigsawSearch(this)" style="height: 442.179px;"> <img
                data-original="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202208/47648ad8b435d8c865a0e1720d67f19e--2484210946.jpg"
                alt="清纯,回眸,养眼" title="关键字：清纯,回眸,养眼"
                data-realurl="http://cdn-hw-static2.shanhutech.cn/bizhi/staticwp/202208/47648ad8b435d8c865a0e1720d67f19e--2484210946.jpg"
                src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsQAAA7EAZUrDhsAAAANSURBVBhXYzh8+PB/AAffA0nNPuCLAAAAAElFTkSuQmCC">
        </div>
    </div> <!-- id="walBox" -->


'''

# 使用BeautifulSoup解析HTML内容
soup = BeautifulSoup(html_fragment, 'html.parser')

# 找到所有的<img>标签
imgs = soup.find_all('img')

# 遍历所有的<img>标签并打印data-original属性
for img in imgs:
    data_original = img.get('data-original')
    if data_original:
        print(data_original)