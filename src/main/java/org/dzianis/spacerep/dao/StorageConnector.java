package org.dzianis.spacerep.dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.spacerep.protos.SpaceRepStateProto;

class StorageConnector {

  private static final Path STATE_FILE_PATH = Path.of("db", "state");

  SpaceRepStateProto readState() {
    try (InputStream is = Files.newInputStream(STATE_FILE_PATH)) {
      return SpaceRepStateProto.parseFrom(is);
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalStateException("Cannot read persistent storage.", e);
    }
  }

  void persistState(SpaceRepStateProto state) {
    try (OutputStream os = Files.newOutputStream(STATE_FILE_PATH)) {
      state.writeTo(os);
      os.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
