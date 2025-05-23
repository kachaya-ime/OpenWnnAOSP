cmake_minimum_required(VERSION 3.4.1)

add_library(WnnEngDic SHARED
            WnnEngDic.c)

include_directories(../libwnnDictionary/include/)

target_link_libraries(WnnEngDic
                      android
                      dl
                      log)
