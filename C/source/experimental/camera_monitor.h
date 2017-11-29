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

#define USE_CAMERA

#ifdef USE_CAMERA
#include "camera.h"
#endif

#define MOVIE_MODE 1
#define IDLE_MODE 0

#define SEND_PIC_STATE 1
#define TAKE_PIC_STATE 0

#define BUFSIZE 500000

struct camera_monitor {
  byte pic_packet[BUFSIZE];
  int mode;
  bool pic_taken;
  bool pic_sent;
  int pic_take_send;
  int  connfd;
  int  connmodefd;
  ssize_t packet_sz;
  #ifdef USE_CAMERA
      camera * cam;
      byte* frame_data;
  #endif
};

struct camera_monitor * cam_mon;

pthread_mutex_t camera_mutex;

int get_pic_take_send(void);

void flip_pic_take_send(void);

void init(void);

void set_mode(int);

bool set_pic_taken(bool);

bool set_pic_sent(bool);

void get_packet(byte *);

void save_packet(byte *);

int get_mode(void);

bool get_pic_taken(void);

bool get_pic_sent(void);

void save_packet_size(ssize_t);

ssize_t get_packet_size(void);
