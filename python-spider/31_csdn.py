from bs4 import BeautifulSoup

# 您提供的HTML内容
html_content = """
<article data-v-6fe2b6a7="" data-v-bb5f5e3e="" class="blog-list-box"><a data-v-6fe2b6a7=""
                                  href="https://blog.csdn.net/interest_ing_/article/details/137895184" target="_blank"
                                  data-report-click="{&quot;spm&quot;:&quot;3001.5502&quot;}"
                                  data-report-query="spm=3001.5502">
                                  <div data-v-6fe2b6a7="" class="blog-img-box"><img data-v-6fe2b6a7=""
                                      src="./洛可可白-CSDN博客_files/f019dbf8ecf84e828949ce702f01f3eb.jpeg" alt=""
                                      class="course-img"></div>
                                  <div data-v-6fe2b6a7="" class="list-box-cont">
                                    <div data-v-6fe2b6a7="">
                                      <div data-v-6fe2b6a7="" class="blog-list-box-top">
                                        <h4 data-v-6fe2b6a7="">
                                          Spring Boot与JdbcTemplate：构建MySQL数据库应用的简易指南
                                        </h4>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="blog-list-content">
                                        Spring是一个开源的Java平台，它为开发Java应用程序提供了全面的基础架构支持。Spring处理了基础设施的问题，使得开发者可以专注于应用程序的开发。Spring的核心特性包括依赖注入（DI）、面向切面编程（AOP）、数据访问、消息传递、测试和更多。Spring的轻量级和灵活性使其成为企业级应用开发的首选框架之一。
                                      </div>
                                    </div>
                                    <div data-v-6fe2b6a7="" class="blog-list-footer">
                                      <div data-v-6fe2b6a7="" class="blog-list-footer-left">
                                        <div data-v-6fe2b6a7="" class="article-type article-type-yc">
                                          原创
                                        </div> <!----> <!----> <!---->
                                        <div data-v-6fe2b6a7="" class="view-time-box">
                                          发布博客&nbsp;19 小时前&nbsp;·
                                        </div>
                                        <div data-v-6fe2b6a7="" class="view-num-box"><span data-v-6fe2b6a7=""
                                            class="view-num">427<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;阅读&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="give-like-box"><span data-v-6fe2b6a7=""
                                            class="give-like-num">16<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;点赞&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">0<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;评论&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">8<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;收藏</span></span></div>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="operate-box"><a data-v-6fe2b6a7=""
                                          href="https://editor.csdn.net/md?articleId=137895184"
                                          data-report-click="{&quot;spm&quot;:&quot;3001.9457&quot;}"
                                          data-report-query="spm=3001.9457" target="_blank"
                                          class="btn-edit-article">编辑</a></div>
                                    </div>
                                  </div>
                                </a></article>
                              <article data-v-6fe2b6a7="" data-v-bb5f5e3e="" class="blog-list-box"><a data-v-6fe2b6a7=""
                                  href="https://blog.csdn.net/interest_ing_/article/details/137846547" target="_blank"
                                  data-report-click="{&quot;spm&quot;:&quot;3001.5502&quot;}"
                                  data-report-query="spm=3001.5502">
                                  <div data-v-6fe2b6a7="" class="blog-img-box"><img data-v-6fe2b6a7=""
                                      src="./洛可可白-CSDN博客_files/47a72f59585746708b693283ac5b2ccc.jpeg" alt=""
                                      class="course-img"></div>
                                  <div data-v-6fe2b6a7="" class="list-box-cont">
                                    <div data-v-6fe2b6a7="">
                                      <div data-v-6fe2b6a7="" class="blog-list-box-top">
                                        <h4 data-v-6fe2b6a7="">
                                          如何从零开始创建React应用：简易指南
                                        </h4>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="blog-list-content">
                                        React是一个流行的JavaScript库，用于构建用户界面。通过使用Create React
                                        App，一个官方提供的脚手架工具，我们可以轻松地创建、开发和部署React应用，而无需深入了解底层构建配置。创建一个React项目通常涉及到使用Node.js和npm（Node包管理器），因为React是一个基于JavaScript的库，而npm帮助我们管理项目中的依赖。
                                      </div>
                                    </div>
                                    <div data-v-6fe2b6a7="" class="blog-list-footer">
                                      <div data-v-6fe2b6a7="" class="blog-list-footer-left">
                                        <div data-v-6fe2b6a7="" class="article-type article-type-yc">
                                          原创
                                        </div> <!----> <!----> <!---->
                                        <div data-v-6fe2b6a7="" class="view-time-box">
                                          发布博客&nbsp;昨天 00:47&nbsp;·
                                        </div>
                                        <div data-v-6fe2b6a7="" class="view-num-box"><span data-v-6fe2b6a7=""
                                            class="view-num">836<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;阅读&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="give-like-box"><span data-v-6fe2b6a7=""
                                            class="give-like-num">27<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;点赞&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">0<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;评论&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">12<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;收藏</span></span></div>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="operate-box"><a data-v-6fe2b6a7=""
                                          href="https://editor.csdn.net/md?articleId=137846547"
                                          data-report-click="{&quot;spm&quot;:&quot;3001.9457&quot;}"
                                          data-report-query="spm=3001.9457" target="_blank"
                                          class="btn-edit-article">编辑</a></div>
                                    </div>
                                  </div>
                                </a></article>
                              <article data-v-6fe2b6a7="" data-v-bb5f5e3e="" class="blog-list-box"><a data-v-6fe2b6a7=""
                                  href="https://blog.csdn.net/interest_ing_/article/details/137846121" target="_blank"
                                  data-report-click="{&quot;spm&quot;:&quot;3001.5502&quot;}"
                                  data-report-query="spm=3001.5502">
                                  <div data-v-6fe2b6a7="" class="blog-img-box"><img data-v-6fe2b6a7=""
                                      src="./洛可可白-CSDN博客_files/9ed84e5a4c8c4ee1ae2556565b4e8f75.png" alt=""
                                      class="course-img"></div>
                                  <div data-v-6fe2b6a7="" class="list-box-cont">
                                    <div data-v-6fe2b6a7="">
                                      <div data-v-6fe2b6a7="" class="blog-list-box-top">
                                        <h4 data-v-6fe2b6a7="">
                                          爆肝3k字！掌握Spring与Redis的高效交互：从Jedis到Spring Data Redis
                                        </h4>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="blog-list-content">
                                        Redis以其卓越的性能和多功能性成为了不可或缺的组件。本文将深入探讨如何在Spring框架中通过Jedis和JedisPool直接操作Redis，以及如何利用Spring
                                        Data Redis简化数据交互过程。我们将从环境搭建开始，逐步介绍依赖引入、代码编写、功能实现，最终通过实例展示如何在Spring
                                        Boot应用中高效地使用Redis。无论你是初学者还是有经验的开发者，本文都将为你提供有价值的见解和实用的操作指南。</div>
                                    </div>
                                    <div data-v-6fe2b6a7="" class="blog-list-footer">
                                      <div data-v-6fe2b6a7="" class="blog-list-footer-left">
                                        <div data-v-6fe2b6a7="" class="article-type article-type-yc">
                                          原创
                                        </div> <!----> <!----> <!---->
                                        <div data-v-6fe2b6a7="" class="view-time-box">
                                          发布博客&nbsp;昨天 00:28&nbsp;·
                                        </div>
                                        <div data-v-6fe2b6a7="" class="view-num-box"><span data-v-6fe2b6a7=""
                                            class="view-num">486<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;阅读&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="give-like-box"><span data-v-6fe2b6a7=""
                                            class="give-like-num">23<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;点赞&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">0<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;评论&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">9<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;收藏</span></span></div>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="operate-box"><a data-v-6fe2b6a7=""
                                          href="https://editor.csdn.net/md?articleId=137846121"
                                          data-report-click="{&quot;spm&quot;:&quot;3001.9457&quot;}"
                                          data-report-query="spm=3001.9457" target="_blank"
                                          class="btn-edit-article">编辑</a></div>
                                    </div>
                                  </div>
                                </a></article>
                              <article data-v-6fe2b6a7="" data-v-bb5f5e3e="" class="blog-list-box"><a data-v-6fe2b6a7=""
                                  href="https://blog.csdn.net/interest_ing_/article/details/137845013" target="_blank"
                                  data-report-click="{&quot;spm&quot;:&quot;3001.5502&quot;}"
                                  data-report-query="spm=3001.5502">
                                  <div data-v-6fe2b6a7="" class="blog-img-box"><img data-v-6fe2b6a7=""
                                      src="./洛可可白-CSDN博客_files/52bce6bce8d242d7b049a0f0d26c0541.png" alt=""
                                      class="course-img"></div>
                                  <div data-v-6fe2b6a7="" class="list-box-cont">
                                    <div data-v-6fe2b6a7="">
                                      <div data-v-6fe2b6a7="" class="blog-list-box-top">
                                        <h4 data-v-6fe2b6a7="">
                                          Spring Boot 多环境配置：YML 文件的三种高效方法
                                        </h4>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="blog-list-content">
                                        在现代软件开发实践中，维护多个环境（如开发、测试和生产）已经成为标准操作。Spring Boot
                                        通过其灵活的配置机制，使得在不同环境中管理应用设置变得简单。尤其是使用 YAML 文件进行配置，它提供了一种简洁、易读的方式来定义应用的配置。本文将探讨在
                                        Spring Boot 中使用 YAML 文件进行多环境配置的三种方法。</div>
                                    </div>
                                    <div data-v-6fe2b6a7="" class="blog-list-footer">
                                      <div data-v-6fe2b6a7="" class="blog-list-footer-left">
                                        <div data-v-6fe2b6a7="" class="article-type article-type-yc">
                                          原创
                                        </div> <!----> <!----> <!---->
                                        <div data-v-6fe2b6a7="" class="view-time-box">
                                          发布博客&nbsp;前天 23:37&nbsp;·
                                        </div>
                                        <div data-v-6fe2b6a7="" class="view-num-box"><span data-v-6fe2b6a7=""
                                            class="view-num">802<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;阅读&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="give-like-box"><span data-v-6fe2b6a7=""
                                            class="give-like-num">6<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;点赞&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">3<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;评论&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">22<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;收藏</span></span></div>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="operate-box"><a data-v-6fe2b6a7=""
                                          href="https://editor.csdn.net/md?articleId=137845013"
                                          data-report-click="{&quot;spm&quot;:&quot;3001.9457&quot;}"
                                          data-report-query="spm=3001.9457" target="_blank"
                                          class="btn-edit-article">编辑</a></div>
                                    </div>
                                  </div>
                                </a></article>
                              <article data-v-6fe2b6a7="" data-v-bb5f5e3e="" class="blog-list-box"><a data-v-6fe2b6a7=""
                                  href="https://blog.csdn.net/interest_ing_/article/details/137759056" target="_blank"
                                  data-report-click="{&quot;spm&quot;:&quot;3001.5502&quot;}"
                                  data-report-query="spm=3001.5502">
                                  <div data-v-6fe2b6a7="" class="blog-img-box"><img data-v-6fe2b6a7=""
                                      src="./洛可可白-CSDN博客_files/30c115e787f9419aaaa893813df080ea.png" alt=""
                                      class="course-img"></div>
                                  <div data-v-6fe2b6a7="" class="list-box-cont">
                                    <div data-v-6fe2b6a7="">
                                      <div data-v-6fe2b6a7="" class="blog-list-box-top">
                                        <h4 data-v-6fe2b6a7="">
                                          Vue项目实战：基于用户身份的动态路由管理
                                        </h4>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="blog-list-content">
                                        在前后端分离项目中，根据不同用户的身份展示不同的路由和页面是一项常见的需求。Vue.js结合vue-router提供了强大的路由管理能力，允许我们根据后端接口的返回数据动态地添加和控制路由。本文将介绍如何在Vue项目中实现基于用户身份的动态路由管理，以及如何利用Vue的module模块来加载这些路由。
                                      </div>
                                    </div>
                                    <div data-v-6fe2b6a7="" class="blog-list-footer">
                                      <div data-v-6fe2b6a7="" class="blog-list-footer-left">
                                        <div data-v-6fe2b6a7="" class="article-type article-type-yc">
                                          原创
                                        </div> <!----> <!----> <!---->
                                        <div data-v-6fe2b6a7="" class="view-time-box">
                                          发布博客&nbsp;2024.04.15&nbsp;·
                                        </div>
                                        <div data-v-6fe2b6a7="" class="view-num-box"><span data-v-6fe2b6a7=""
                                            class="view-num">696<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;阅读&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="give-like-box"><span data-v-6fe2b6a7=""
                                            class="give-like-num">11<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;点赞&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">0<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;评论&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">11<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;收藏</span></span></div>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="operate-box"><a data-v-6fe2b6a7=""
                                          href="https://editor.csdn.net/md?articleId=137759056"
                                          data-report-click="{&quot;spm&quot;:&quot;3001.9457&quot;}"
                                          data-report-query="spm=3001.9457" target="_blank"
                                          class="btn-edit-article">编辑</a></div>
                                    </div>
                                  </div>
                                </a></article>
                              <article data-v-6fe2b6a7="" data-v-bb5f5e3e="" class="blog-list-box"><a data-v-6fe2b6a7=""
                                  href="https://blog.csdn.net/interest_ing_/article/details/137758784" target="_blank"
                                  data-report-click="{&quot;spm&quot;:&quot;3001.5502&quot;}"
                                  data-report-query="spm=3001.5502">
                                  <div data-v-6fe2b6a7="" class="blog-img-box"><img data-v-6fe2b6a7=""
                                      src="./洛可可白-CSDN博客_files/216a9c107ad6448dad49dda32ecdd6a6.png" alt=""
                                      class="course-img"></div>
                                  <div data-v-6fe2b6a7="" class="list-box-cont">
                                    <div data-v-6fe2b6a7="">
                                      <div data-v-6fe2b6a7="" class="blog-list-box-top">
                                        <h4 data-v-6fe2b6a7="">
                                          Vite多环境配置与打包：灵活高效的Vue开发工作流
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
                                          发布博客&nbsp;2024.04.15&nbsp;·
                                        </div>
                                        <div data-v-6fe2b6a7="" class="view-num-box"><span data-v-6fe2b6a7=""
                                            class="view-num">1025<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;阅读&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="give-like-box"><span data-v-6fe2b6a7=""
                                            class="give-like-num">32<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;点赞&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">0<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;评论&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">22<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;收藏</span></span></div>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="operate-box"><a data-v-6fe2b6a7=""
                                          href="https://editor.csdn.net/md?articleId=137758784"
                                          data-report-click="{&quot;spm&quot;:&quot;3001.9457&quot;}"
                                          data-report-query="spm=3001.9457" target="_blank"
                                          class="btn-edit-article">编辑</a></div>
                                    </div>
                                  </div>
                                </a></article>
                              <article data-v-6fe2b6a7="" data-v-bb5f5e3e="" class="blog-list-box"><a data-v-6fe2b6a7=""
                                  href="https://blog.csdn.net/interest_ing_/article/details/137730085" target="_blank"
                                  data-report-click="{&quot;spm&quot;:&quot;3001.5502&quot;}"
                                  data-report-query="spm=3001.5502">
                                  <div data-v-6fe2b6a7="" class="blog-img-box"><img data-v-6fe2b6a7=""
                                      src="./洛可可白-CSDN博客_files/58d8493a201a463b8470da40cad720fe.png" alt=""
                                      class="course-img"></div>
                                  <div data-v-6fe2b6a7="" class="list-box-cont">
                                    <div data-v-6fe2b6a7="">
                                      <div data-v-6fe2b6a7="" class="blog-list-box-top">
                                        <h4 data-v-6fe2b6a7="">
                                          前端实现自动获取农历日期：探索JavaScript的跨文化编程
                                        </h4>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="blog-list-content">
                                        在全球化的今天，编写能够适应不同文化和地区的软件变得尤为重要。JavaScript作为前端开发的核心语言，不仅能够处理日常的交互逻辑，还能实现一些特定文化的日期计算，例如农历日期。本文将介绍如何使用JavaScript在前端自动获取并展示当前的农历日期，同时扩展我们的知识面，探索跨文化编程的重要性。
                                      </div>
                                    </div>
                                    <div data-v-6fe2b6a7="" class="blog-list-footer">
                                      <div data-v-6fe2b6a7="" class="blog-list-footer-left">
                                        <div data-v-6fe2b6a7="" class="article-type article-type-yc">
                                          原创
                                        </div> <!----> <!----> <!---->
                                        <div data-v-6fe2b6a7="" class="view-time-box">
                                          发布博客&nbsp;2024.04.14&nbsp;·
                                        </div>
                                        <div data-v-6fe2b6a7="" class="view-num-box"><span data-v-6fe2b6a7=""
                                            class="view-num">495<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;阅读&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="give-like-box"><span data-v-6fe2b6a7=""
                                            class="give-like-num">12<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;点赞&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">0<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;评论&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">3<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;收藏</span></span></div>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="operate-box"><a data-v-6fe2b6a7=""
                                          href="https://editor.csdn.net/md?articleId=137730085"
                                          data-report-click="{&quot;spm&quot;:&quot;3001.9457&quot;}"
                                          data-report-query="spm=3001.9457" target="_blank"
                                          class="btn-edit-article">编辑</a></div>
                                    </div>
                                  </div>
                                </a></article>
                              <article data-v-6fe2b6a7="" data-v-bb5f5e3e="" class="blog-list-box"><a data-v-6fe2b6a7=""
                                  href="https://blog.csdn.net/interest_ing_/article/details/137729892" target="_blank"
                                  data-report-click="{&quot;spm&quot;:&quot;3001.5502&quot;}"
                                  data-report-query="spm=3001.5502">
                                  <div data-v-6fe2b6a7="" class="blog-img-box"><img data-v-6fe2b6a7=""
                                      src="./洛可可白-CSDN博客_files/40b97c7c6af641c4880d1377e983c740.png" alt=""
                                      class="course-img"></div>
                                  <div data-v-6fe2b6a7="" class="list-box-cont">
                                    <div data-v-6fe2b6a7="">
                                      <div data-v-6fe2b6a7="" class="blog-list-box-top">
                                        <h4 data-v-6fe2b6a7="">
                                          自定义滚动条样式：前端实现跨浏览器兼容
                                        </h4>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="blog-list-content">
                                        滚动条不仅是用户与网页交互的重要元素，也是网页美观性的一个细节体现。在前端开发中，自定义滚动条样式可以提升用户体验，使网页更加个性化。本文将介绍如何在前端设置滚轮滚动条样式，并确保兼容各大主流浏览器。
                                      </div>
                                    </div>
                                    <div data-v-6fe2b6a7="" class="blog-list-footer">
                                      <div data-v-6fe2b6a7="" class="blog-list-footer-left">
                                        <div data-v-6fe2b6a7="" class="article-type article-type-yc">
                                          原创
                                        </div> <!----> <!----> <!---->
                                        <div data-v-6fe2b6a7="" class="view-time-box">
                                          发布博客&nbsp;2024.04.14&nbsp;·
                                        </div>
                                        <div data-v-6fe2b6a7="" class="view-num-box"><span data-v-6fe2b6a7=""
                                            class="view-num">1009<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;阅读&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="give-like-box"><span data-v-6fe2b6a7=""
                                            class="give-like-num">36<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;点赞&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">0<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;评论&nbsp;·</span></span></div>
                                        <div data-v-6fe2b6a7="" class="comment-box"><span data-v-6fe2b6a7=""
                                            class="comment-num">10<span data-v-6fe2b6a7=""
                                              class="two-px">&nbsp;收藏</span></span></div>
                                      </div>
                                      <div data-v-6fe2b6a7="" class="operate-box"><a data-v-6fe2b6a7=""
                                          href="https://editor.csdn.net/md?articleId=137729892"
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
        # '>': "**[" + h4_text + "]" + "(" + href_odd + ")**",
        # '>-': view_num_content + " " + give_like_num_content + " " + comment_num_content,
        '| ': "**[" + h4_text + "]" + "(" + href_odd + ")**" + '| ',
    }

#     示例
# | **[Element-Plus下拉菜单边框去除教程](https://blog.csdn.net/interest_ing_/article/details/137030937)** |
# | :----------------------------------------------------------- |
# | **[MyBatis-Plus分页接口实现教程：Spring Boot中如何编写分页查询](https://blog.csdn.net/interest_ing_/article/details/137062699)** |
# | **[MyBatis-Plus分页接口实现教程：Spring Boot中如何编写分页查询](https://blog.csdn.net/interest_ing_/article/details/137062699)** |
# | **[MyBatis-Plus分页接口实现教程：Spring Boot中如何编写分页查询](https://blog.csdn.net/interest_ing_/article/details/137062699)** |
# | **[MyBatis-Plus分页接口实现教程：Spring Boot中如何编写分页查询](https://blog.csdn.net/interest_ing_/article/details/137062699)** |

# 打印结果
for article_title, data in results.items():
    # print(f'{article_title}:')
    for key, value in data.items():
        print(f'  {key} {value}')
    # 打印空行以分隔不同的article块结果
    # print()

