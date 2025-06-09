from bs4 import BeautifulSoup

# 您提供的HTML内容
html_content = """

<article data-v-6fe2b6a7="" data-v-bb5f5e3e="" class="blog-list-box"><a data-v-6fe2b6a7=""
                                  href="https://blog.csdn.net/interest_ing_/article/details/137252150" target="_blank"
                                  data-report-click="{&quot;spm&quot;:&quot;3001.5502&quot;}"
                                  data-report-query="spm=3001.5502">
                                  <div data-v-6fe2b6a7="" class="blog-img-box"><img data-v-6fe2b6a7=""
                                      src="./洛可可白-CSDN博客_files/b52291b25ab04d45bf01849c08f043c9.png" alt=""
                                      class="course-img"></div>
                                  <div data-v-6fe2b6a7="" class="list-box-cont">
                                    <div data-v-6fe2b6a7="">
                                      <div data-v-6fe2b6a7="" class="blog-list-box-top">
                                        <h4 data-v-6fe2b6a7="">
                                          入门教程：Windows搭建C语言和EasyX开发环境
                                        </h4>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="blog-list-content">Visual Studio
                                      </div>
                                    </div>
                                    <div data-v-6fe2b6a7="" class="blog-list-footer">
                                      <div data-v-6fe2b6a7="" class="blog-list-footer-left">
                                        <div data-v-6fe2b6a7="" class="article-type article-type-yc">
                                          原创
                                        </div> <!----> <!----> <!---->
                                        <div data-v-6fe2b6a7="" class="view-time-box">
                                          发布博客&nbsp;21 小时前&nbsp;·
                                        </div>
                                        <div data-v-6fe2b6a7="" class="view-num-box"><span data-v-6fe2b6a7=""
                                            class="view-num">414<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;阅读&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="give-like-box"><span data-v-6fe2b6a7=""
                                            class="give-like-num">13<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;点赞&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">0<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;评论&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">22<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;收藏</span></span></div>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="operate-box"><a data-v-6fe2b6a7=""
                                          href="https://editor.csdn.net/md?articleId=137252150"
                                          data-report-click="{&quot;spm&quot;:&quot;3001.9457&quot;}"
                                          data-report-query="spm=3001.9457" target="_blank"
                                          class="btn-edit-article">编辑</a></div>
                                    </div>
                                  </div>
                                </a></article>
                              <article data-v-6fe2b6a7="" data-v-bb5f5e3e="" class="blog-list-box"><a data-v-6fe2b6a7=""
                                  href="https://blog.csdn.net/interest_ing_/article/details/137212826" target="_blank"
                                  data-report-click="{&quot;spm&quot;:&quot;3001.5502&quot;}"
                                  data-report-query="spm=3001.5502">
                                  <div data-v-6fe2b6a7="" class="blog-img-box"><img data-v-6fe2b6a7=""
                                      src="./洛可可白-CSDN博客_files/1078a16d755a44738b92263aab22c0f8.png" alt=""
                                      class="course-img"></div>
                                  <div data-v-6fe2b6a7="" class="list-box-cont">
                                    <div data-v-6fe2b6a7="">
                                      <div data-v-6fe2b6a7="" class="blog-list-box-top">
                                        <h4 data-v-6fe2b6a7="">
                                          CentOS系统下Docker的安装教程
                                        </h4>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="blog-list-content">
                                        Docker是一个开源的应用容器引擎，它允许开发人员将应用及其依赖打包到一个轻量级、可移植的容器中，然后发布到任何流行的Linux机器或Windows机器上，也可以实现虚拟化。在本文中，我们将探讨如何在CentOS系统上安装Docker。
                                      </div>
                                    </div>
                                    <div data-v-6fe2b6a7="" class="blog-list-footer">
                                      <div data-v-6fe2b6a7="" class="blog-list-footer-left">
                                        <div data-v-6fe2b6a7="" class="article-type article-type-yc">
                                          原创
                                        </div> <!----> <!----> <!---->
                                        <div data-v-6fe2b6a7="" class="view-time-box">
                                          发布博客&nbsp;前天 23:34&nbsp;·
                                        </div>
                                        <div data-v-6fe2b6a7="" class="view-num-box"><span data-v-6fe2b6a7=""
                                            class="view-num">842<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;阅读&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="give-like-box"><span data-v-6fe2b6a7=""
                                            class="give-like-num">24<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;点赞&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">0<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;评论&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">13<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;收藏</span></span></div>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="operate-box"><a data-v-6fe2b6a7=""
                                          href="https://editor.csdn.net/md?articleId=137212826"
                                          data-report-click="{&quot;spm&quot;:&quot;3001.9457&quot;}"
                                          data-report-query="spm=3001.9457" target="_blank"
                                          class="btn-edit-article">编辑</a></div>
                                    </div>
                                  </div>
                                </a></article>
                              <article data-v-6fe2b6a7="" data-v-bb5f5e3e="" class="blog-list-box"><a data-v-6fe2b6a7=""
                                  href="https://blog.csdn.net/interest_ing_/article/details/137188833" target="_blank"
                                  data-report-click="{&quot;spm&quot;:&quot;3001.5502&quot;}"
                                  data-report-query="spm=3001.5502">
                                  <div data-v-6fe2b6a7="" class="blog-img-box"><img data-v-6fe2b6a7=""
                                      src="./洛可可白-CSDN博客_files/1bb63e4591364c779e00d46d1e28de1f.png" alt=""
                                      class="course-img"></div>
                                  <div data-v-6fe2b6a7="" class="list-box-cont">
                                    <div data-v-6fe2b6a7="">
                                      <div data-v-6fe2b6a7="" class="blog-list-box-top">
                                        <h4 data-v-6fe2b6a7="">
                                          Spring Boot单元测试全指南：使用Mockito和AssertJ
                                        </h4>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="blog-list-content">
                                        在现代软件开发实践中，单元测试是不可或缺的一环，它帮助我们确保代码的可靠性和稳定性。对于使用Spring
                                        Boot构建的应用，编写单元测试不仅可以验证业务逻辑的正确性，还可以确保服务的健壮性。本文将详细介绍如何在Spring
                                        Boot项目中进行单元测试，包括使用Mockito进行依赖模拟和使用AssertJ进行断言。</div>
                                    </div>
                                    <div data-v-6fe2b6a7="" class="blog-list-footer">
                                      <div data-v-6fe2b6a7="" class="blog-list-footer-left">
                                        <div data-v-6fe2b6a7="" class="article-type article-type-yc">
                                          原创
                                        </div> <!----> <!----> <!---->
                                        <div data-v-6fe2b6a7="" class="view-time-box">
                                          发布博客&nbsp;前天 01:37&nbsp;·
                                        </div>
                                        <div data-v-6fe2b6a7="" class="view-num-box"><span data-v-6fe2b6a7=""
                                            class="view-num">641<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;阅读&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="give-like-box"><span data-v-6fe2b6a7=""
                                            class="give-like-num">3<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;点赞&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">0<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;评论&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">5<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;收藏</span></span></div>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="operate-box"><a data-v-6fe2b6a7=""
                                          href="https://editor.csdn.net/md?articleId=137188833"
                                          data-report-click="{&quot;spm&quot;:&quot;3001.9457&quot;}"
                                          data-report-query="spm=3001.9457" target="_blank"
                                          class="btn-edit-article">编辑</a></div>
                                    </div>
                                  </div>
                                </a></article>
                              <article data-v-6fe2b6a7="" data-v-bb5f5e3e="" class="blog-list-box"><a data-v-6fe2b6a7=""
                                  href="https://blog.csdn.net/interest_ing_/article/details/137128037" target="_blank"
                                  data-report-click="{&quot;spm&quot;:&quot;3001.5502&quot;}"
                                  data-report-query="spm=3001.5502">
                                  <div data-v-6fe2b6a7="" class="blog-img-box"><img data-v-6fe2b6a7=""
                                      src="./洛可可白-CSDN博客_files/358b07d2a08b439ba7e8ec6ce0a15b9f.png" alt=""
                                      class="course-img"></div>
                                  <div data-v-6fe2b6a7="" class="list-box-cont">
                                    <div data-v-6fe2b6a7="">
                                      <div data-v-6fe2b6a7="" class="blog-list-box-top">
                                        <h4 data-v-6fe2b6a7="">
                                          Yarn简介及Windows安装与使用指南
                                        </h4>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="blog-list-content">
                                      </div>
                                    </div>
                                    <div data-v-6fe2b6a7="" class="blog-list-footer">
                                      <div data-v-6fe2b6a7="" class="blog-list-footer-left">
                                        <div data-v-6fe2b6a7="" class="article-type article-type-yc">
                                          原创
                                        </div> <!----> <!----> <!---->
                                        <div data-v-6fe2b6a7="" class="view-time-box">
                                          发布博客&nbsp;2024.03.28&nbsp;·
                                        </div>
                                        <div data-v-6fe2b6a7="" class="view-num-box"><span data-v-6fe2b6a7=""
                                            class="view-num">995<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;阅读&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="give-like-box"><span data-v-6fe2b6a7=""
                                            class="give-like-num">28<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;点赞&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">0<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;评论&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">30<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;收藏</span></span></div>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="operate-box"><a data-v-6fe2b6a7=""
                                          href="https://editor.csdn.net/md?articleId=137128037"
                                          data-report-click="{&quot;spm&quot;:&quot;3001.9457&quot;}"
                                          data-report-query="spm=3001.9457" target="_blank"
                                          class="btn-edit-article">编辑</a></div>
                                    </div>
                                  </div>
                                </a></article>
                              <article data-v-6fe2b6a7="" data-v-bb5f5e3e="" class="blog-list-box"><a data-v-6fe2b6a7=""
                                  href="https://blog.csdn.net/interest_ing_/article/details/137107173" target="_blank"
                                  data-report-click="{&quot;spm&quot;:&quot;3001.5502&quot;}"
                                  data-report-query="spm=3001.5502">
                                  <div data-v-6fe2b6a7="" class="blog-img-box"><img data-v-6fe2b6a7=""
                                      src="./洛可可白-CSDN博客_files/49fa4943eb54478c86f7b499be3e0028.png" alt=""
                                      class="course-img"></div>
                                  <div data-v-6fe2b6a7="" class="list-box-cont">
                                    <div data-v-6fe2b6a7="">
                                      <div data-v-6fe2b6a7="" class="blog-list-box-top">
                                        <h4 data-v-6fe2b6a7="">
                                          H5实现3D旋转照片墙教程
                                        </h4>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="blog-list-content">
                                        CSS的`transform`属性允许我们对元素进行2D或3D的转换。这包括旋转、缩放、移动和倾斜等操作。在3D转换中，`rotateX`、`rotateY`和`translateZ`是常用的函数，它们分别用于围绕X轴和Y轴旋转以及沿Z轴移动。
                                      </div>
                                    </div>
                                    <div data-v-6fe2b6a7="" class="blog-list-footer">
                                      <div data-v-6fe2b6a7="" class="blog-list-footer-left">
                                        <div data-v-6fe2b6a7="" class="article-type article-type-yc">
                                          原创
                                        </div> <!----> <!----> <!---->
                                        <div data-v-6fe2b6a7="" class="view-time-box">
                                          发布博客&nbsp;2024.03.28&nbsp;·
                                        </div>
                                        <div data-v-6fe2b6a7="" class="view-num-box"><span data-v-6fe2b6a7=""
                                            class="view-num">469<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;阅读&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="give-like-box"><span data-v-6fe2b6a7=""
                                            class="give-like-num">10<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;点赞&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">0<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;评论&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">3<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;收藏</span></span></div>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="operate-box"><a data-v-6fe2b6a7=""
                                          href="https://editor.csdn.net/md?articleId=137107173"
                                          data-report-click="{&quot;spm&quot;:&quot;3001.9457&quot;}"
                                          data-report-query="spm=3001.9457" target="_blank"
                                          class="btn-edit-article">编辑</a></div>
                                    </div>
                                  </div>
                                </a></article>
                              <article data-v-6fe2b6a7="" data-v-bb5f5e3e="" class="blog-list-box"><a data-v-6fe2b6a7=""
                                  href="https://blog.csdn.net/interest_ing_/article/details/137062740" target="_blank"
                                  data-report-click="{&quot;spm&quot;:&quot;3001.5502&quot;}"
                                  data-report-query="spm=3001.5502">
                                  <div data-v-6fe2b6a7="" class="blog-img-box"><img data-v-6fe2b6a7=""
                                      src="./洛可可白-CSDN博客_files/c1b09586911a460983b2eeec79232a72.png" alt=""
                                      class="course-img"></div>
                                  <div data-v-6fe2b6a7="" class="list-box-cont">
                                    <div data-v-6fe2b6a7="">
                                      <div data-v-6fe2b6a7="" class="blog-list-box-top">
                                        <h4 data-v-6fe2b6a7="">
                                          Element-Plus 实现动态渲染图标教程
                                        </h4>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="blog-list-content">Element-Plus 是 Element UI 的 Vue
                                        3 版本，它提供了一套完整的组件库，用于快速构建企业级的后台产品。在 Element-Plus 中，我们可以使用 ``
                                        标签来动态渲染组件，这使得在菜单中根据条件动态显示不同的图标成为可能。本文将介绍如何使用 Element-Plus 和 Vue.js 来实现动态渲染图标的功能。
                                      </div>
                                    </div>
                                    <div data-v-6fe2b6a7="" class="blog-list-footer">
                                      <div data-v-6fe2b6a7="" class="blog-list-footer-left">
                                        <div data-v-6fe2b6a7="" class="article-type article-type-yc">
                                          原创
                                        </div> <!----> <!----> <!---->
                                        <div data-v-6fe2b6a7="" class="view-time-box">
                                          发布博客&nbsp;2024.03.27&nbsp;·
                                        </div>
                                        <div data-v-6fe2b6a7="" class="view-num-box"><span data-v-6fe2b6a7=""
                                            class="view-num">818<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;阅读&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="give-like-box"><span data-v-6fe2b6a7=""
                                            class="give-like-num">33<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;点赞&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">0<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;评论&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">19<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;收藏</span></span></div>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="operate-box"><a data-v-6fe2b6a7=""
                                          href="https://editor.csdn.net/md?articleId=137062740"
                                          data-report-click="{&quot;spm&quot;:&quot;3001.9457&quot;}"
                                          data-report-query="spm=3001.9457" target="_blank"
                                          class="btn-edit-article">编辑</a></div>
                                    </div>
                                  </div>
                                </a></article>
                              <article data-v-6fe2b6a7="" data-v-bb5f5e3e="" class="blog-list-box"><a data-v-6fe2b6a7=""
                                  href="https://blog.csdn.net/interest_ing_/article/details/137062699" target="_blank"
                                  data-report-click="{&quot;spm&quot;:&quot;3001.5502&quot;}"
                                  data-report-query="spm=3001.5502">
                                  <div data-v-6fe2b6a7="" class="blog-img-box"><img data-v-6fe2b6a7=""
                                      src="./洛可可白-CSDN博客_files/219a46e0ecb743808e72921249f31c09.png" alt=""
                                      class="course-img"></div>
                                  <div data-v-6fe2b6a7="" class="list-box-cont">
                                    <div data-v-6fe2b6a7="">
                                      <div data-v-6fe2b6a7="" class="blog-list-box-top">
                                        <h4 data-v-6fe2b6a7="">
                                          MyBatis-Plus分页接口实现教程：Spring Boot中如何编写分页查询
                                        </h4>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="blog-list-content">MyBatis-Plus 是一个 MyBatis 的增强工具，在
                                        MyBatis 的基础上只做增强不做改变，为简化开发、提高效率而生。它提供了强大的分页插件，可以轻松实现分页查询的功能。在 Spring Boot 项目中使用
                                        MyBatis-Plus 可以大大简化分页逻辑的编写。本文将介绍如何在 Spring Boot 项目中使用 MyBatis-Plus 实现分页接口。</div>
                                    </div>
                                    <div data-v-6fe2b6a7="" class="blog-list-footer">
                                      <div data-v-6fe2b6a7="" class="blog-list-footer-left">
                                        <div data-v-6fe2b6a7="" class="article-type article-type-yc">
                                          原创
                                        </div> <!----> <!----> <!---->
                                        <div data-v-6fe2b6a7="" class="view-time-box">
                                          发布博客&nbsp;2024.03.27&nbsp;·
                                        </div>
                                        <div data-v-6fe2b6a7="" class="view-num-box"><span data-v-6fe2b6a7=""
                                            class="view-num">897<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;阅读&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="give-like-box"><span data-v-6fe2b6a7=""
                                            class="give-like-num">17<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;点赞&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">1<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;评论&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">7<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;收藏</span></span></div>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="operate-box"><a data-v-6fe2b6a7=""
                                          href="https://editor.csdn.net/md?articleId=137062699"
                                          data-report-click="{&quot;spm&quot;:&quot;3001.9457&quot;}"
                                          data-report-query="spm=3001.9457" target="_blank"
                                          class="btn-edit-article">编辑</a></div>
                                    </div>
                                  </div>
                                </a></article>
                              <article data-v-6fe2b6a7="" data-v-bb5f5e3e="" class="blog-list-box"><a data-v-6fe2b6a7=""
                                  href="https://blog.csdn.net/interest_ing_/article/details/137030937" target="_blank"
                                  data-report-click="{&quot;spm&quot;:&quot;3001.5502&quot;}"
                                  data-report-query="spm=3001.5502">
                                  <div data-v-6fe2b6a7="" class="blog-img-box"><img data-v-6fe2b6a7=""
                                      src="./洛可可白-CSDN博客_files/9f7844d5b10846efbd77daa6348f6326.jpeg" alt=""
                                      class="course-img"></div>
                                  <div data-v-6fe2b6a7="" class="list-box-cont">
                                    <div data-v-6fe2b6a7="">
                                      <div data-v-6fe2b6a7="" class="blog-list-box-top">
                                        <h4 data-v-6fe2b6a7="">
                                          Element-Plus下拉菜单边框去除教程
                                        </h4>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="blog-list-content">Element-Plus 是 Element UI 的 Vue
                                        3 版本，它提供了一套完整的组件库，用于快速构建企业级的后台产品。在使用 Element-Plus
                                        进行开发时，我们可能会遇到需要自定义组件样式的情况，比如去除下拉框在聚焦时的默认边框。本文将介绍如何使用 CSS 来去除 Element-Plus
                                        下拉框的边框，并简要介绍 Element-Plus 以及 Vue 的相关概念。</div>
                                    </div>
                                    <div data-v-6fe2b6a7="" class="blog-list-footer">
                                      <div data-v-6fe2b6a7="" class="blog-list-footer-left">
                                        <div data-v-6fe2b6a7="" class="article-type article-type-yc">
                                          原创
                                        </div> <!----> <!----> <!---->
                                        <div data-v-6fe2b6a7="" class="view-time-box">
                                          发布博客&nbsp;2024.03.26&nbsp;·
                                        </div>
                                        <div data-v-6fe2b6a7="" class="view-num-box"><span data-v-6fe2b6a7=""
                                            class="view-num">803<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;阅读&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="give-like-box"><span data-v-6fe2b6a7=""
                                            class="give-like-num">21<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;点赞&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">0<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;评论&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">28<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;收藏</span></span></div>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="operate-box"><a data-v-6fe2b6a7=""
                                          href="https://editor.csdn.net/md?articleId=137030937"
                                          data-report-click="{&quot;spm&quot;:&quot;3001.9457&quot;}"
                                          data-report-query="spm=3001.9457" target="_blank"
                                          class="btn-edit-article">编辑</a></div>
                                    </div>
                                  </div>
                                </a></article>
                              <article data-v-6fe2b6a7="" data-v-bb5f5e3e="" class="blog-list-box"><a data-v-6fe2b6a7=""
                                  href="https://blog.csdn.net/interest_ing_/article/details/137030873" target="_blank"
                                  data-report-click="{&quot;spm&quot;:&quot;3001.5502&quot;}"
                                  data-report-query="spm=3001.5502">
                                  <div data-v-6fe2b6a7="" class="blog-img-box"><img data-v-6fe2b6a7=""
                                      src="./洛可可白-CSDN博客_files/784c01b4dea14079be7ddc2571d2fd51.png" alt=""
                                      class="course-img"></div>
                                  <div data-v-6fe2b6a7="" class="list-box-cont">
                                    <div data-v-6fe2b6a7="">
                                      <div data-v-6fe2b6a7="" class="blog-list-box-top">
                                        <h4 data-v-6fe2b6a7="">
                                          Web实现猜数字游戏：JavaScript DOM基础与实例教程
                                        </h4>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="blog-list-content">
                                        猜数字游戏是一个简单而经典的游戏，玩家需要猜测由系统随机生成的一个数字。在本教程中，我们将学习如何使用JavaScript和DOM来实现这个网页版的猜数字游戏，并介绍相关的JavaScript
                                        DOM基础知识。</div>
                                    </div>
                                    <div data-v-6fe2b6a7="" class="blog-list-footer">
                                      <div data-v-6fe2b6a7="" class="blog-list-footer-left">
                                        <div data-v-6fe2b6a7="" class="article-type article-type-yc">
                                          原创
                                        </div> <!----> <!----> <!---->
                                        <div data-v-6fe2b6a7="" class="view-time-box">
                                          发布博客&nbsp;2024.03.26&nbsp;·
                                        </div>
                                        <div data-v-6fe2b6a7="" class="view-num-box"><span data-v-6fe2b6a7=""
                                            class="view-num">792<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;阅读&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="give-like-box"><span data-v-6fe2b6a7=""
                                            class="give-like-num">11<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;点赞&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">0<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;评论&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">9<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;收藏</span></span></div>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="operate-box"><a data-v-6fe2b6a7=""
                                          href="https://editor.csdn.net/md?articleId=137030873"
                                          data-report-click="{&quot;spm&quot;:&quot;3001.9457&quot;}"
                                          data-report-query="spm=3001.9457" target="_blank"
                                          class="btn-edit-article">编辑</a></div>
                                    </div>
                                  </div>
                                </a></article>
                              <article data-v-6fe2b6a7="" data-v-bb5f5e3e="" class="blog-list-box"><a data-v-6fe2b6a7=""
                                  href="https://blog.csdn.net/interest_ing_/article/details/136999903" target="_blank"
                                  data-report-click="{&quot;spm&quot;:&quot;3001.5502&quot;}"
                                  data-report-query="spm=3001.5502">
                                  <div data-v-6fe2b6a7="" class="blog-img-box"><img data-v-6fe2b6a7=""
                                      src="./洛可可白-CSDN博客_files/9c2ea2990b9647a2b070450c0985c324.png" alt=""
                                      class="course-img"></div>
                                  <div data-v-6fe2b6a7="" class="list-box-cont">
                                    <div data-v-6fe2b6a7="">
                                      <div data-v-6fe2b6a7="" class="blog-list-box-top">
                                        <h4 data-v-6fe2b6a7="">
                                          Web实现名言生成器：JavaScript DOM基础与实例教程
                                        </h4>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="blog-list-content">
                                        名言生成器是一个简单而有趣的Web应用，它可以随机显示历史上著名人物的名言。通过这个教程，我们将学习如何使用JavaScript DOM
                                        API来实现这个功能，并介绍相关的JavaScript DOM基础知识。DOM（Document Object
                                        Model）是HTML文档的编程接口，它允许我们通过JavaScript访问和操作网页元素。在JavaScript中，我们可以使用DOM
                                        API来获取元素、修改内容、绑定事件等。</div>
                                    </div>
                                    <div data-v-6fe2b6a7="" class="blog-list-footer">
                                      <div data-v-6fe2b6a7="" class="blog-list-footer-left">
                                        <div data-v-6fe2b6a7="" class="article-type article-type-yc">
                                          原创
                                        </div> <!----> <!----> <!---->
                                        <div data-v-6fe2b6a7="" class="view-time-box">
                                          发布博客&nbsp;2024.03.25&nbsp;·
                                        </div>
                                        <div data-v-6fe2b6a7="" class="view-num-box"><span data-v-6fe2b6a7=""
                                            class="view-num">1109<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;阅读&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="give-like-box"><span data-v-6fe2b6a7=""
                                            class="give-like-num">21<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;点赞&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">0<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;评论&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">18<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;收藏</span></span></div>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="operate-box"><a data-v-6fe2b6a7=""
                                          href="https://editor.csdn.net/md?articleId=136999903"
                                          data-report-click="{&quot;spm&quot;:&quot;3001.9457&quot;}"
                                          data-report-query="spm=3001.9457" target="_blank"
                                          class="btn-edit-article">编辑</a></div>
                                    </div>
                                  </div>
                                </a></article>
                              <article data-v-6fe2b6a7="" data-v-bb5f5e3e="" class="blog-list-box"><a data-v-6fe2b6a7=""
                                  href="https://blog.csdn.net/interest_ing_/article/details/136999561" target="_blank"
                                  data-report-click="{&quot;spm&quot;:&quot;3001.5502&quot;}"
                                  data-report-query="spm=3001.5502">
                                  <div data-v-6fe2b6a7="" class="blog-img-box"><img data-v-6fe2b6a7=""
                                      src="./洛可可白-CSDN博客_files/bd89e20e4d5a477085b664dbaa288fd1.png" alt=""
                                      class="course-img"></div>
                                  <div data-v-6fe2b6a7="" class="list-box-cont">
                                    <div data-v-6fe2b6a7="">
                                      <div data-v-6fe2b6a7="" class="blog-list-box-top">
                                        <h4 data-v-6fe2b6a7="">
                                          Web实现井字棋游戏：JavaScript DOM基础与实例教程
                                        </h4>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="blog-list-content">
                                        井字棋（Tic-Tac-Toe）是一款经典的两人对战游戏，适合作为学习JavaScript
                                        DOM操作的实践项目。本文将通过一个简单的实例，介绍如何使用JavaScript和DOM API来实现一个井字棋游戏，并讲解相关的JavaScript
                                        DOM基础知识。</div>
                                    </div>
                                    <div data-v-6fe2b6a7="" class="blog-list-footer">
                                      <div data-v-6fe2b6a7="" class="blog-list-footer-left">
                                        <div data-v-6fe2b6a7="" class="article-type article-type-yc">
                                          原创
                                        </div> <!----> <!----> <!---->
                                        <div data-v-6fe2b6a7="" class="view-time-box">
                                          发布博客&nbsp;2024.03.25&nbsp;·
                                        </div>
                                        <div data-v-6fe2b6a7="" class="view-num-box"><span data-v-6fe2b6a7=""
                                            class="view-num">716<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;阅读&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="give-like-box"><span data-v-6fe2b6a7=""
                                            class="give-like-num">29<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;点赞&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">0<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;评论&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">18<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;收藏</span></span></div>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="operate-box"><a data-v-6fe2b6a7=""
                                          href="https://editor.csdn.net/md?articleId=136999561"
                                          data-report-click="{&quot;spm&quot;:&quot;3001.9457&quot;}"
                                          data-report-query="spm=3001.9457" target="_blank"
                                          class="btn-edit-article">编辑</a></div>
                                    </div>
                                  </div>
                                </a></article>
                              <article data-v-6fe2b6a7="" data-v-bb5f5e3e="" class="blog-list-box"><a data-v-6fe2b6a7=""
                                  href="https://blog.csdn.net/interest_ing_/article/details/136978506" target="_blank"
                                  data-report-click="{&quot;spm&quot;:&quot;3001.5502&quot;}"
                                  data-report-query="spm=3001.5502">
                                  <div data-v-6fe2b6a7="" class="blog-img-box"><img data-v-6fe2b6a7=""
                                      src="./洛可可白-CSDN博客_files/6fbb5f32660c43eea450ccad38c11f58.png" alt=""
                                      class="course-img"></div>
                                  <div data-v-6fe2b6a7="" class="list-box-cont">
                                    <div data-v-6fe2b6a7="">
                                      <div data-v-6fe2b6a7="" class="blog-list-box-top">
                                        <h4 data-v-6fe2b6a7="">
                                          Web实现表格单选全选与反选操作：JavaScript DOM基础与实例教程
                                        </h4>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="blog-list-content">DOM（Document Object
                                        Model）是文档对象模型的缩写，它将HTML或XML文档视为树结构，每个节点都是文档的一部分，可以是文档本身、元素、属性或文本内容。JavaScript中的DOM
                                        API提供了大量的方法和属性，允许开发者动态地访问和更新文档的内容、结构和样式。</div>
                                    </div>
                                    <div data-v-6fe2b6a7="" class="blog-list-footer">
                                      <div data-v-6fe2b6a7="" class="blog-list-footer-left">
                                        <div data-v-6fe2b6a7="" class="article-type article-type-yc">
                                          原创
                                        </div> <!----> <!----> <!---->
                                        <div data-v-6fe2b6a7="" class="view-time-box">
                                          发布博客&nbsp;2024.03.24&nbsp;·
                                        </div>
                                        <div data-v-6fe2b6a7="" class="view-num-box"><span data-v-6fe2b6a7=""
                                            class="view-num">879<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;阅读&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="give-like-box"><span data-v-6fe2b6a7=""
                                            class="give-like-num">16<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;点赞&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">0<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;评论&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">9<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;收藏</span></span></div>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="operate-box"><a data-v-6fe2b6a7=""
                                          href="https://editor.csdn.net/md?articleId=136978506"
                                          data-report-click="{&quot;spm&quot;:&quot;3001.9457&quot;}"
                                          data-report-query="spm=3001.9457" target="_blank"
                                          class="btn-edit-article">编辑</a></div>
                                    </div>
                                  </div>
                                </a></article>
                              <article data-v-6fe2b6a7="" data-v-bb5f5e3e="" class="blog-list-box"><a data-v-6fe2b6a7=""
                                  href="https://blog.csdn.net/interest_ing_/article/details/136978442" target="_blank"
                                  data-report-click="{&quot;spm&quot;:&quot;3001.5502&quot;}"
                                  data-report-query="spm=3001.5502">
                                  <div data-v-6fe2b6a7="" class="blog-img-box"><img data-v-6fe2b6a7=""
                                      src="./洛可可白-CSDN博客_files/61cc79513edb48a2b93dd8d5e68205a8.png" alt=""
                                      class="course-img"></div>
                                  <div data-v-6fe2b6a7="" class="list-box-cont">
                                    <div data-v-6fe2b6a7="">
                                      <div data-v-6fe2b6a7="" class="blog-list-box-top">
                                        <h4 data-v-6fe2b6a7="">
                                          H5实现Web ECharts教程：轻松创建动态数据图表
                                        </h4>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="blog-list-content">
                                        ECharts是一款由百度前端团队开发的开源可视化库，它提供了丰富的图表类型和灵活的配置选项，使得开发者能够轻松地在Web页面上创建交互式的图表。无论是简单的柱状图、折线图，还是复杂的关系图、地图，ECharts都能够提供强大的支持。
                                      </div>
                                    </div>
                                    <div data-v-6fe2b6a7="" class="blog-list-footer">
                                      <div data-v-6fe2b6a7="" class="blog-list-footer-left">
                                        <div data-v-6fe2b6a7="" class="article-type article-type-yc">
                                          原创
                                        </div> <!----> <!----> <!---->
                                        <div data-v-6fe2b6a7="" class="view-time-box">
                                          发布博客&nbsp;2024.03.24&nbsp;·
                                        </div>
                                        <div data-v-6fe2b6a7="" class="view-num-box"><span data-v-6fe2b6a7=""
                                            class="view-num">1246<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;阅读&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="give-like-box"><span data-v-6fe2b6a7=""
                                            class="give-like-num">21<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;点赞&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">0<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;评论&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">9<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;收藏</span></span></div>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="operate-box"><a data-v-6fe2b6a7=""
                                          href="https://editor.csdn.net/md?articleId=136978442"
                                          data-report-click="{&quot;spm&quot;:&quot;3001.9457&quot;}"
                                          data-report-query="spm=3001.9457" target="_blank"
                                          class="btn-edit-article">编辑</a></div>
                                    </div>
                                  </div>
                                </a></article>



"""

# 使用BeautifulSoup解析HTML
soup = BeautifulSoup(html_content, 'html.parser')

# 查找所有的article标签
articles = soup.find_all('article', class_='blog-list-box')

# 初始化一个字典来存储结果
results = {}

# 遍历每个article块
for index, article in enumerate(articles, start=1):
    # 提取第奇数个<a>元素的href属性
    a_tag = article.find('a')
    href_odd = a_tag.get('href') if a_tag else None

    # 提取<h4>标签中的文字
    h4_tag = article.find('h4')
    h4_text = h4_tag.get_text(strip=True) if h4_tag else None

    # 提取class为view-num的元素内容
    view_num_tag = article.find('span', class_='view-num')
    view_num_content = view_num_tag.get_text(strip=True) if view_num_tag else None

    # 提取class为give-like-num的元素内容
    give_like_num_tag = article.find('span', class_='give-like-num')
    give_like_num_content = give_like_num_tag.get_text(strip=True) if give_like_num_tag else None

    # 提取class为comment-num的第二个元素的内容
    comment_num_tags = article.find_all('span', class_='comment-num')
    comment_num_content = comment_num_tags[1].get_text(strip=True) if len(comment_num_tags) > 1 else None

    # 将结果存储到字典中
    # results[f'Article {index}'] = {
    #     '文章链接': href_odd,
    #     '文章标题': h4_text,
    #     '阅读数': view_num_content,
    #     '点赞数': give_like_num_content,
    #     '收藏数': comment_num_content
    # }

    results[f'Article {index}'] = {
        # '>': index.__str__() + ". " + "**[" + h4_text + "]" + "(" + href_odd + ")**",
        '>': "**[" + h4_text + "]" + "(" + href_odd + ")**",
        '>-': view_num_content + " " + give_like_num_content + " " + comment_num_content,
    }

# 打印结果
for article_title, data in results.items():
    # print(f'{article_title}:')
    for key, value in data.items():
        print(f'  {key} {value}')
    # 打印空行以分隔不同的article块结果
    print()
