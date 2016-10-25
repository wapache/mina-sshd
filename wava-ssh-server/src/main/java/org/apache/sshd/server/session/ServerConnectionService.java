/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sshd.server.session;

import org.apache.sshd.agent.common.AgentForwardSupport;
import org.apache.sshd.agent.common.DefaultAgentForwardSupport;
import org.apache.sshd.common.SshException;
import org.apache.sshd.common.session.helpers.AbstractConnectionService;
import org.apache.sshd.common.util.ValidateUtils;
import org.apache.sshd.server.x11.DefaultX11ForwardSupport;
import org.apache.sshd.server.x11.X11ForwardSupport;

/**
 * Server side <code>ssh-connection</code> service.
 *
 * @author <a href="mailto:dev@mina.apache.org">Apache MINA SSHD Project</a>
 */
public class ServerConnectionService
        extends AbstractConnectionService<AbstractServerSession>
        implements ServerSessionHolder {
    protected ServerConnectionService(AbstractServerSession s) throws SshException {
        super(s);

        if (!s.isAuthenticated()) {
            throw new SshException("Session is not authenticated");
        }
    }

    @Override
    public final ServerSession getServerSession() {
        return getSession();
    }
    
    
//  @Override
  public X11ForwardSupport getX11ForwardSupport() {
      X11ForwardSupport x11Support;
      AbstractServerSession session = getSession();
      synchronized (x11ForwardHolder) {
          x11Support = x11ForwardHolder.get();
          if (x11Support != null) {
              return x11Support;
          }

          x11Support = ValidateUtils.checkNotNull(createX11ForwardSupport(session), "No X11 forwarder created for %s", session);
          x11ForwardHolder.set(x11Support);
      }

      if (log.isDebugEnabled()) {
          log.debug("getX11ForwardSupport({}) created instance", session);
      }
      return x11Support;
  }

  protected X11ForwardSupport createX11ForwardSupport(AbstractServerSession session) {
      return new DefaultX11ForwardSupport(this);
  }

//  @Override
  public AgentForwardSupport getAgentForwardSupport() {
      AgentForwardSupport agentForward;
      AbstractServerSession session = getSession();
      synchronized (agentForwardHolder) {
          agentForward = agentForwardHolder.get();
          if (agentForward != null) {
              return agentForward;
          }

          agentForward = ValidateUtils.checkNotNull(createAgentForwardSupport(session), "No agent forward created for %s", session);
          agentForwardHolder.set(agentForward);
      }

      if (log.isDebugEnabled()) {
          log.debug("getAgentForwardSupport({}) created instance", session);
      }

      return agentForward;
  }

  protected AgentForwardSupport createAgentForwardSupport(AbstractServerSession session) {
      return new DefaultAgentForwardSupport(this);
  }
}
