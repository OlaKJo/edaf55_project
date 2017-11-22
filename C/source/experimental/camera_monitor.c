struct camera_monitor {
  byte pictureData[BUFSIZE];
  int mode;
  bool pic_taken;
  bool pic_sent;
};

struct camera_monitor * cam_mon;
pthread_mutex_t camera_mutex;

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
