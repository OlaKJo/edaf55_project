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

#define MOVIE_MODE 1
#define IDLE_MODE 0

#define BUFSIZE 50000
typedef char byte;

struct camera_monitor {
  byte pictureData[BUFSIZE];
  int mode;
  bool pic_taken;
  bool pic_sent;
};

struct camera_monitor * cam_mon;

pthread_mutex_t camera_mutex;

void init(void);

void set_mode(int);

bool set_pic_taken(bool);

bool set_pic_sent(bool);

void get_pic(byte *);

void save_pic(byte *);

int get_mode(void);

bool get_pic_taken(void);

bool get_pic_sent(void);
