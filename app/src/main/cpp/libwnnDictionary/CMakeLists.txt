cmake_minimum_required(VERSION 3.4.1)

add_library(wnndict SHARED
            OpenWnnDictionaryImplJni.c
            engine/ndapi.c
            engine/neapi.c
            engine/ndbdic.c
            engine/ndfdic.c
            engine/ndldic.c
            engine/ndrdic.c
            engine/necode.c
            engine/ndcommon.c
            engine/nj_str.c)

include_directories(include/)

target_link_libraries(wnndict
                      android
                      dl
                      log)
