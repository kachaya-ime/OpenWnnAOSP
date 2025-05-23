cmake_minimum_required(VERSION 3.4.1)

add_library(WnnJpnDic SHARED
            WnnJpnDic.c)

include_directories(../libwnnDictionary/include/)

target_link_libraries(WnnJpnDic
                      android
                      dl
                      log)
