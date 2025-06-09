from bs4 import BeautifulSoup

# 您提供的HTML内容
html_content = """
 <article data-v-6fe2b6a7="" data-v-bb5f5e3e="" class="blog-list-box"><a data-v-6fe2b6a7=""
        href="https://blog.csdn.net/interest_ing_/article/details/125730385?spm=1001.2014.3001.5502&#34;     
        target="_blank" data-report-click="{&quot;spm&quot;:&quot;3001.5502&quot;}"
        data-report-query="spm=3001.5502">
        <div data-v-6fe2b6a7="" class="blog-img-box"><img data-v-6fe2b6a7=""
            src="./洛可可白-CSDN博客_files/f15328fc311e4cf0af9ed30069b466fb.png" alt=""
            class="course-img"></div>
        <div data-v-6fe2b6a7="" class="list-box-cont">
          <div data-v-6fe2b6a7="">
            <div data-v-6fe2b6a7="" class="blog-list-box-top">
              <h4 data-v-6fe2b6a7="">
                Color-UI 简介及使用教程
              </h4>
            </div>
            <div data-v-6fe2b6a7="" class="blog-list-content">
              小程序开发最好用的组件库——ColorUI；用过就离不开了！这是一个鲜亮的高饱和色彩，专注视觉的小程序组件库
              。ColorUI是一个css库！！！在你引入样式后可以根据class来调用组件，一些含有交互的操作我也有简单写，可以为你开发提供一些思路。需要你引入样式后可以根据class来调用使用。只需要在HTML或wxml标签中加入想要样式的class类名，就可以调用组件库中提前编辑好的样式，大大提高了开发效率。
            </div>
          </div>
          <div data-v-6fe2b6a7="" class="blog-list-footer">
            <div data-v-6fe2b6a7="" class="blog-list-footer-left">
              <div data-v-6fe2b6a7="" class="article-type article-type-yc">
                原创
              </div> <!----> <!----> <!---->
              <div data-v-6fe2b6a7="" class="view-time-box">
                发布博客&nbsp;2022.07.11&nbsp;·
              </div>
              <div data-v-6fe2b6a7="" class="view-num-box"><span data-v-6fe2b6a7=""
                  class="view-num">5902<span data-v-6fe2b6a7=""
                    class="two-px">&nbsp;阅读&nbsp;·</span></span></div>
              <div data-v-6fe2b6a7="" class="give-like-box"><span data-v-6fe2b6a7=""
                  class="give-like-num">2<span data-v-6fe2b6a7=""
                    class="two-px">&nbsp;点赞&nbsp;·</span></span></div>
              <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                  class="comment-num">0<span data-v-6fe2b6a7=""
                    class="two-px">&nbsp;评论&nbsp;·</span></span></div>
              <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                  class="comment-num">13<span data-v-6fe2b6a7=""
                    class="two-px">&nbsp;收藏</span></span></div>
            </div>
            <div data-v-6fe2b6a7="" class="operate-box"><a data-v-6fe2b6a7=""
                href="https://mp.csdn.net/console/editor/html/125730385&#34;     
                data-report-click="{&quot;spm&quot;:&quot;3001.9457&quot;}"
                data-report-query="spm=3001.9457" target="_blank"
                class="btn-edit-article">编辑</a></div>
          </div>
        </div>
      </a></article>

 <article data-v-6fe2b6a7="" data-v-bb5f5e3e="" class="blog-list-box"><a data-v-6fe2b6a7=""
        href="https://blog.csdn.net/interest_ing_/article/details/125730385?spm=1001.2014.3001.5502&#34;      
        target="_blank" data-report-click="{&quot;spm&quot;:&quot;3001.5502&quot;}"
        data-report-query="spm=3001.5502">
        <div data-v-6fe2b6a7="" class="blog-img-box"><img data-v-6fe2b6a7=""
            src="./洛可可白-CSDN博客_files/f15328fc311e4cf0af9ed30069b466fb.png" alt=""
            class="course-img"></div>
        <div data-v-6fe2b6a7="" class="list-box-cont">
          <div data-v-6fe2b6a7="">
            <div data-v-6fe2b6a7="" class="blog-list-box-top">
              <h4 data-v-6fe2b6a7="">
                Color-UI 简介及使用教程
              </h4>
            </div>
            <div data-v-6fe2b6a7="" class="blog-list-content">
              小程序开发最好用的组件库——ColorUI；用过就离不开了！这是一个鲜亮的高饱和色彩，专注视觉的小程序组件库
              。ColorUI是一个css库！！！在你引入样式后可以根据class来调用组件，一些含有交互的操作我也有简单写，可以为你开发提供一些思路。需要你引入样式后可以根据class来调用使用。只需要在HTML或wxml标签中加入想要样式的class类名，就可以调用组件库中提前编辑好的样式，大大提高了开发效率。
            </div>
          </div>
          <div data-v-6fe2b6a7="" class="blog-list-footer">
            <div data-v-6fe2b6a7="" class="blog-list-footer-left">
              <div data-v-6fe2b6a7="" class="article-type article-type-yc">
                原创
              </div> <!----> <!----> <!---->
              <div data-v-6fe2b6a7="" class="view-time-box">
                发布博客&nbsp;2022.07.11&nbsp;·
              </div>
              <div data-v-6fe2b6a7="" class="view-num-box"><span data-v-6fe2b6a7=""
                  class="view-num">5902<span data-v-6fe2b6a7=""
                    class="two-px">&nbsp;阅读&nbsp;·</span></span></div>
              <div data-v-6fe2b6a7="" class="give-like-box"><span data-v-6fe2b6a7=""
                  class="give-like-num">2<span data-v-6fe2b6a7=""
                    class="two-px">&nbsp;点赞&nbsp;·</span></span></div>
              <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                  class="comment-num">0<span data-v-6fe2b6a7=""
                    class="two-px">&nbsp;评论&nbsp;·</span></span></div>
              <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                  class="comment-num">13<span data-v-6fe2b6a7=""
                    class="two-px">&nbsp;收藏</span></span></div>
            </div>
            <div data-v-6fe2b6a7="" class="operate-box"><a data-v-6fe2b6a7=""
                href="https://mp.csdn.net/console/editor/html/125730385&#34;      
                data-report-click="{&quot;spm&quot;:&quot;3001.9457&quot;}"
                data-report-query="spm=3001.9457" target="_blank"
                class="btn-edit-article">编辑</a></div>
          </div>
        </div>
      </a></article>
"""

# 使用BeautifulSoup解析HTML
soup = BeautifulSoup(html_content, 'html.parser')

# 提取<a>标签中的href属性
a_tag = soup.find('a')
href = a_tag.get('href') if a_tag else None
print(f'Href: {href}')

# 提取<h4>标签中的文本
h4_tag = soup.find('h4')
h4_text = h4_tag.get_text(strip=True) if h4_tag else None
print(f'H4 Text: {h4_text}')

# 提取具有特定class属性的元素内容
view_num = soup.find('span', class_='view-num')
give_like_num = soup.find('span', class_='give-like-num')

# 查找所有具有class属性为comment-num的span元素
comment_nums = soup.find_all('span', class_='comment-num')

# 提取第二个元素的内容
second_comment_num = comment_nums[1].get_text(strip=True) if len(comment_nums) > 1 else None

print(f'View Num: {view_num.get_text(strip=True) if view_num else None}')
print(f'Give Like Num: {give_like_num.get_text(strip=True) if give_like_num else None}')
print(f'第二个comment-num的内容: {second_comment_num}')