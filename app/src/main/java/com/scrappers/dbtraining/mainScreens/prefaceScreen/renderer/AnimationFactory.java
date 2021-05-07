package com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.TransformTrack;
import com.jme3.app.Application;
import com.jme3.app.jmeSurfaceView.JmeSurfaceView;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Quaternion;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.scrappers.dbtraining.R;
import com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.AnimLayers;
import com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.BasicArmature;
import com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.BasicTween;
import com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.BlendableAnimation;
import com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.BlenderTween;
import com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.EmitterTween;
import com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.SimpleScaleTrack;
import com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.StackLoops;
import com.scrappers.superiorExtendedEngine.menuStates.UiStateManager;
import com.scrappers.superiorExtendedEngine.menuStates.UiStatesLooper;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_SHORT;
import static com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.Scene.defaultOne;
import static com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.Scene.defaultStack;
import static com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.Scene.defaultThree;
import static com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.Scene.defaultTwo;

/**
 * @author pavl_g
 */
public class AnimationFactory extends BaseAppState implements View.OnClickListener {
    private final Spatial dataBaseStack;
    private final JmeSurfaceView jmeSurfaceView;
    private RelativeLayout menu;
    private UiStateManager uiStateManager;
    private SimpleScaleTrack simpleScaleTrack;
    private static final float REANIMATE_TIME=2f;
    private float timer=0.0f;
    public AnimationFactory(Spatial dataBaseStack, JmeSurfaceView jmeSurfaceView) {
        this.dataBaseStack=dataBaseStack;
        this.jmeSurfaceView=jmeSurfaceView;
    }
    @Override
    protected void initialize(Application app) {
        ((AppCompatActivity)jmeSurfaceView.getContext()).runOnUiThread(()->{
            uiStateManager=new UiStateManager(((RelativeLayout)jmeSurfaceView.getParent()));
            ((RelativeLayout)jmeSurfaceView.getParent()).findViewById(R.id.animationSettings).setOnClickListener(this);
            ((RelativeLayout)jmeSurfaceView.getParent()).findViewById(R.id.reset).setOnClickListener(this);

                menu = (RelativeLayout) uiStateManager.attachUiState(uiStateManager.fromXML(R.layout.animation_selection_menu));
                menu.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                menu.animate().translationYBy(1200).setDuration(500).start();
                menu.setVisibility(GONE);


                menu.findViewById(R.id.closeMenu).setOnClickListener(this);
                menu.findViewById(R.id.simpleTrack).setOnClickListener(this);
                menu.findViewById(R.id.stackLoops).setOnClickListener(this);
                menu.findViewById(R.id.armatureTwist).setOnClickListener(this);
                menu.findViewById(R.id.basicTween).setOnClickListener(this);
                menu.findViewById(R.id.particleEmitter).setOnClickListener(this);
                menu.findViewById(R.id.bottleFall).setOnClickListener(this);
                menu.findViewById(R.id.blenderAnimations).setOnClickListener(this);
                menu.findViewById(R.id.armatureMask).setOnClickListener(this);

        });
        simpleScaleTrack = new SimpleScaleTrack("SimpleScaleTrack",dataBaseStack);
        simpleScaleTrack.setAnimationEvents(new SimpleScaleTrack.AnimationEvents() {
            @Override
            public void onAnimationStart(AnimComposer animComposer, TransformTrack transformationStart) {
                ((Activity)jmeSurfaceView.getContext()).runOnUiThread(()->{
                    Toast.makeText(jmeSurfaceView.getContext(),"Track Started",LENGTH_SHORT).show();
                });
            }

            @Override
            public void onAnimationShuffle(AnimComposer animComposer, TransformTrack transform, int transform1, int transform2) {
                ((Activity)jmeSurfaceView.getContext()).runOnUiThread(()->{
                    Toast.makeText(jmeSurfaceView.getContext(),"Track Shuffled" + transform1+" "+transform2,LENGTH_SHORT).show();
                });

            }

            @Override
            public void onAnimationEnd(AnimComposer animComposer, TransformTrack transformEnd) {
                ((Activity)jmeSurfaceView.getContext()).runOnUiThread(()->{
                    Toast.makeText(jmeSurfaceView.getContext(),"Track Finished",LENGTH_SHORT).show();
                });
            }
        });
        reAttachAllStates();
    }

    @Override
    protected void cleanup(Application app) {
        ((Activity)menu.getContext()).runOnUiThread(()->{
            menu.setVisibility(INVISIBLE);
            menu.animate().translationYBy(1200).setDuration(500).start();
            ((RelativeLayout)jmeSurfaceView.getParent()).findViewById(R.id.animationSettings).animate().setDuration(600).rotation(-45).start();
            uiStateManager.forEachUiState((UiStatesLooper.Modifiable.Looper) (currentView, position) -> {
                deActivateButton(currentView);
                uiStateManager.detachUiState(currentView);
                if(position == uiStateManager.getLastStateIndex()){
                    uiStateManager.detachUiManager();
                }
            });
        });
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }

    @Override
    public void update(float tpf) {
        timer+=tpf;
        if(timer>REANIMATE_TIME){
            timer=0;
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.animationSettings){

            ((RelativeLayout)jmeSurfaceView.getParent()).findViewById(R.id.animationSettings).animate().setDuration(100).rotation(45).start();
            menu.setVisibility(VISIBLE);
            menu.animate().translationYBy(-1200).setDuration(600).start();

        }
        if(v.getId()==R.id.reset){
            jmeSurfaceView.getLegacyApplication().enqueue(()->{
                AnimComposer animComposer = dataBaseStack.getControl(AnimComposer.class);
                dataBaseStack.removeControl(animComposer);
                reAttachAllStates();
            });
            uiStateManager.forEachUiState((UiStatesLooper.Modifiable.Looper) (currentView, position) -> {
                deActivateButton(currentView.findViewById(R.id.simpleTrack));
                deActivateButton(currentView.findViewById(R.id.stackLoops));
                deActivateButton(currentView.findViewById(R.id.armatureTwist));
                deActivateButton(currentView.findViewById(R.id.armatureMask));
                deActivateButton(currentView.findViewById(R.id.basicTween));
                deActivateButton(currentView.findViewById(R.id.particleEmitter));
                deActivateButton(currentView.findViewById(R.id.bottleFall));
                deActivateButton(currentView.findViewById(R.id.blenderAnimations));
            });
            Toast.makeText(jmeSurfaceView.getContext(),"Resettled the Scene !",Toast.LENGTH_LONG).show();

        }else if(v.getId()==R.id.closeMenu){

            ((RelativeLayout)jmeSurfaceView.getParent()).findViewById(R.id.animationSettings).animate().setDuration(600).rotation(-45).start();
            menu.animate().translationYBy(1200).setDuration(600).withEndAction(()->menu.setVisibility(INVISIBLE)).start();

        }else if(v.getId()==R.id.simpleTrack){

            //getStateManager().detach(getStateManager().getState("SimpleAnimation",SimpleScaleTrack.class)); -> not working ??!
            if(!getStateManager().getState(SimpleScaleTrack.class).isEnabled()){
                getStateManager().getState(SimpleScaleTrack.class).setEnabled(true);
                activateButton(v);
            }else{
                getStateManager().getState(SimpleScaleTrack.class).setEnabled(false);
                deActivateButton(v);
            }

        }else if(v.getId()==R.id.stackLoops){

            if(!getStateManager().getState(StackLoops.class).isEnabled()){
                getStateManager().getState(StackLoops.class).setEnabled(true);
                activateButton(v);
            }else{
                getStateManager().getState(StackLoops.class).setEnabled(false);
                deActivateButton(v);
            }

        }else if(v.getId()==R.id.armatureTwist){

            if(!getStateManager().getState(BasicArmature.class).isEnabled()){
                getStateManager().getState(BasicArmature.class).setEnabled(true);
                activateButton(v);
            }else{
                getStateManager().getState(BasicArmature.class).setEnabled(false);
                deActivateButton(v);
            }

        }else if(v.getId()==R.id.basicTween){

            if(!getStateManager().getState(BasicTween.class).isEnabled()){
                getStateManager().getState(BasicTween.class).setEnabled(true);
                activateButton(v);
            }else{
                getStateManager().getState(BasicTween.class).setEnabled(false);
                deActivateButton(v);
            }

        }else if(v.getId()==R.id.particleEmitter){

            if(!getStateManager().getState(EmitterTween.class).isEnabled()){
                getStateManager().getState(EmitterTween.class).setEnabled(true);
                activateButton(v);
            }else{
                getStateManager().getState(EmitterTween.class).setEnabled(false);
                deActivateButton(v);
            }

        }else if(v.getId()==R.id.bottleFall){

            if(!getStateManager().getState(BlendableAnimation.class).isEnabled()){
                getStateManager().getState(BlendableAnimation.class).setEnabled(true);
                activateButton(v);
            }else{
                getStateManager().getState(BlendableAnimation.class).setEnabled(false);
                deActivateButton(v);
            }

        }else if(v.getId()==R.id.blenderAnimations){

            if(!getStateManager().getState(BlenderTween.class).isEnabled()){
                getStateManager().getState(BlenderTween.class).setEnabled(true);
                activateButton(v);
            }else{
                getStateManager().getState(BlenderTween.class).setEnabled(false);
                deActivateButton(v);
            }

        }else if(v.getId()==R.id.armatureMask){

            if(!getStateManager().getState(AnimLayers.class).isEnabled()){
                getStateManager().getState(AnimLayers.class).setEnabled(true);
                activateButton(v);
            }else{
                getStateManager().getState(AnimLayers.class).setEnabled(false);
                deActivateButton(v);
            }

        }
    }
    /**
     * Changes the UI settings of the activated animation
     * @param v the android view to change it's UI.
     */
    protected void activateButton(View v){
        ((GradientDrawable) Objects.requireNonNull(
                ContextCompat.getDrawable(v.getContext(), R.drawable.toolbar_background))).setColors(new int[]{
                ContextCompat.getColor(v.getContext(),R.color.greenGold),ContextCompat.getColor(v.getContext(),
                R.color.greenGold),ContextCompat.getColor(v.getContext(),R.color.greenGold)});
        v.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.toolbar_background));
        v.invalidate();
        Toast.makeText(jmeSurfaceView.getContext(),"Animation Started",Toast.LENGTH_LONG).show();
    }
    /**
     * Changes the UI settings of the deactivated animation
     * @param v the android view to change it's UI.
     */
    protected void deActivateButton(View v){
        ((GradientDrawable) Objects.requireNonNull(
                ContextCompat.getDrawable(v.getContext(), R.drawable.toolbar_background))).setColors(new int[]{
                ContextCompat.getColor(v.getContext(),R.color.lightBlack),ContextCompat.getColor(v.getContext(),
                R.color.lightBlack),ContextCompat.getColor(v.getContext(),R.color.lightBlack)});
        v.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.toolbar_background));
        v.invalidate();
        Toast.makeText(jmeSurfaceView.getContext(),"Animation Stopped",Toast.LENGTH_LONG).show();
    }

    protected void reAttachAllStates(){
        jmeSurfaceView.getLegacyApplication().enqueue(()->{
            final AnimComposer animComposer = new AnimComposer();
            animComposer.setEnabled(false);
            dataBaseStack.addControl(animComposer);
            if(getStateManager().hasState(getStateManager().getState(SimpleScaleTrack.class))){
                getStateManager().detach(getStateManager().getState(SimpleScaleTrack.class));
                getStateManager().detach(getStateManager().getState(StackLoops.class));
                getStateManager().detach(getStateManager().getState(BasicArmature.class));
                getStateManager().detach(getStateManager().getState(BasicTween.class));
                getStateManager().detach(getStateManager().getState(EmitterTween.class));
                getStateManager().detach(getStateManager().getState(BlendableAnimation.class));
                getStateManager().detach(getStateManager().getState(BlenderTween.class));
                getStateManager().detach(getStateManager().getState(AnimLayers.class));
            }
            if(!getStateManager().hasState(getStateManager().getState(SimpleScaleTrack.class))){
                getStateManager().attach(simpleScaleTrack);
                getStateManager().attach(new StackLoops("StackLoopsTrack", dataBaseStack));
                getStateManager().attach(new BasicArmature("BasicArmatureTrack", dataBaseStack));
                getStateManager().attach(new BasicTween("BasicTweenTrack", dataBaseStack));
                getStateManager().attach(new EmitterTween("EmitterTween", dataBaseStack));
                getStateManager().attach(new BlendableAnimation("SimulateBottleFall", dataBaseStack));
                getStateManager().attach(new BlenderTween("BlenderImport", dataBaseStack));
                getStateManager().attach(new AnimLayers("MultipleAnimLayers", dataBaseStack));
            }
            ((Node)dataBaseStack).getChild("Cylinder.001").setLocalTransform(defaultOne);
            ((Node)dataBaseStack).getChild("Cylinder.002").setLocalTransform(defaultTwo);
            ((Node)dataBaseStack).getChild("Cylinder.003").setLocalTransform(defaultThree);
            dataBaseStack.setLocalTransform(defaultStack);
        });
    }
}
