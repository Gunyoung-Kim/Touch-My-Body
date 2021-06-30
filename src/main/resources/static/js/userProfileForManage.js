const goToUserComments = (userId) => {
  location.href = '/manager/usermanage/' + userId +'/comments';
}

const goToUserPosts = (userId) => {
  location.href = '/manager/usermanage/' + userId + '/posts';
}
