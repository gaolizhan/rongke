package com.rongke.web.shiro;

/**
 * Created by cww on 2017/2/26.
 */
public class RedisSessionDao  {
//    @Resource
//    private RedisClusterCache redisClusterCache;
//
//    Logger log = LoggerFactory.getLogger(getClass());
//
//    @Override
//    public void update(Session session) throws UnknownSessionException {
//        log.info("更新seesion,id=[{}]", session.getId().toString());
//        try {
//            redisClusterCache.putCache(session.getId().toString(), session);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void delete(Session session) {
//        log.info("删除seesion,id=[{}]", session.getId().toString());
//        try {
//            redisClusterCache.delCache(session.getId().toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Override
//    public Collection<Session> getActiveSessions() {
//        log.info("获取存活的session");
//        return Collections.emptySet();
//    }
//
//    @Override
//    protected Serializable doCreate(Session session) {
//        Serializable sessionId = generateSessionId(session);
//        assignSessionId(session, sessionId);
//        log.info("创建seesion,id=[{}]", session.getId().toString());
//        try {
//            redisClusterCache.putCache(sessionId.toString(), session);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//        return sessionId;
//    }
//
//    @Override
//    protected Session doReadSession(Serializable sessionId) {
//
//        log.info("获取seesion,id=[{}]", sessionId.toString());
//        Session session = null;
//        try {
//            session = redisClusterCache.getCache(sessionId.toString());
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//        return session;
//    }
}