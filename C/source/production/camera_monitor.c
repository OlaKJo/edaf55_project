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

void init(void) {
  cam_mon = malloc(sizeof(*cam_mon));
  cam_mon->pic_sent = true;
  cam_mon->pic_taken = false;
  cam_mon->pic_take_send = 0;
  cam_mon->mode = IDLE_MODE;
}

int get_pic_take_send(void) {
  pthread_mutex_lock(&camera_mutex);
  int current = cam_mon->pic_take_send;
  pthread_mutex_unlock(&camera_mutex);
  return current;
}

void flip_pic_take_send(void) {
  pthread_mutex_lock(&camera_mutex);
  if(cam_mon->pic_take_send == 1)
    cam_mon->pic_take_send = 0;
  else
    cam_mon->pic_take_send = 1;
  pthread_mutex_unlock(&camera_mutex);
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

void get_packet(byte * pic_copy) {
  pthread_mutex_lock(&camera_mutex);
  memcpy(pic_copy, cam_mon->pic_packet, BUFSIZE);
  pthread_mutex_unlock(&camera_mutex);
}

void save_packet(byte * pic) {
  pthread_mutex_lock(&camera_mutex);
  memcpy(cam_mon->pic_packet, pic, BUFSIZE);
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

void save_packet_size(ssize_t packet_sz) {
  pthread_mutex_lock(&camera_mutex);
  cam_mon->packet_sz = packet_sz;
  pthread_mutex_unlock(&camera_mutex);
}

ssize_t get_packet_size(void) {
  pthread_mutex_lock(&camera_mutex);
  ssize_t size = cam_mon->packet_sz;
  pthread_mutex_unlock(&camera_mutex);
  return size;
}
