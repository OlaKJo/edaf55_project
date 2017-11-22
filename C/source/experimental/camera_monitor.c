#include "camera_monitor.h"
#include <sys/socket.h>
#include <netinet/in.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>  // for memcpy, memset, strlen
#include <poll.h>
#include <pthread.h>
#include <unistd.h>
#include <stdbool.h>

#ifdef USE_CAMERA
#include "camera.h"
#endif

void init(void) {
  cam_mon = malloc(sizeof(*cam_mon));
  cam_mon->pic_sent = false;
  cam_mon->pic_taken = false;
  cam_mon->mode = IDLE_MODE;
}

void set_mode(int val) {
  pthread_mutex_lock(&camera_mutex);
  cam_mon->mode = val;
  pthread_mutex_unlock(&camera_mutex);
}

bool set_pic_taken(bool val) {
  pthread_mutex_lock(&camera_mutex);
  cam_mon->pic_taken = val;
  pthread_mutex_unlock(&camera_mutex);
  return cam_mon->pic_taken;
}

bool set_pic_sent(bool val) {
  pthread_mutex_lock(&camera_mutex);
  cam_mon->pic_sent = val;
  pthread_mutex_unlock(&camera_mutex);
  return cam_mon->pic_sent;
}

void get_pic(byte * pic_copy) {
  pthread_mutex_lock(&camera_mutex);
  memcpy(pic_copy, cam_mon->pictureData, BUFSIZE);
  pthread_mutex_unlock(&camera_mutex);
}

void save_pic(byte * pic) {
  pthread_mutex_lock(&camera_mutex);
  memcpy(cam_mon->pictureData, pic, BUFSIZE);
  pthread_mutex_unlock(&camera_mutex);
}

int get_mode(void) {
  pthread_mutex_lock(&camera_mutex);
  int current_mode = cam_mon->mode;
  pthread_mutex_unlock(&camera_mutex);
  return current_mode;
}

bool get_pic_taken(void) {
  pthread_mutex_lock(&camera_mutex);
  bool current_pic_taken = cam_mon->pic_taken;
  pthread_mutex_unlock(&camera_mutex);
  return current_pic_taken;
}

bool get_pic_sent(void) {
  pthread_mutex_lock(&camera_mutex);
  bool current_pic_sent = cam_mon->pic_sent;
  pthread_mutex_unlock(&camera_mutex);
  return current_pic_sent;
}
