/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package dawgswig;

public class DawgSwig {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected DawgSwig(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(DawgSwig obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  @SuppressWarnings("deprecation")
  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        DawgSwigMdlJNI.delete_DawgSwig(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public DawgSwig(String filename) {
    this(DawgSwigMdlJNI.new_DawgSwig(filename), true);
  }

  public void Insert(String word) {
    DawgSwigMdlJNI.DawgSwig_Insert(swigCPtr, this, word);
  }

  public void Finish() {
    DawgSwigMdlJNI.DawgSwig_Finish(swigCPtr, this);
  }

}
